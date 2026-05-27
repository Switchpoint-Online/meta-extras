# chromium-x11_%.bbappend
#
# Align V8 / JIT build behaviour with Raspberry Pi OS (Debian Bookworm) so
# that Chromium works reliably on the Cortex-A72 (BCM2711, CM4).
#
# Root cause of SIGSEGV during WhatsApp Web WASM initialisation
# ──────────────────────────────────────────────────────────────
# meta-browser sets is_official_build=true.  On 64-bit targets this
# implicitly enables two problematic features:
#
#  1. V8 Sandbox (v8_enable_sandbox=true by default on 64-bit official builds)
#     The sandbox reserves a ~1 TB virtual address region at startup and
#     requires all JIT-compiled code to live inside it.  If that reservation
#     fails (memory layout restrictions on this 4 GB Pi) V8 proceeds silently
#     but every subsequent write to the "sandbox code space" faults →
#     SIGSEGV in chromium-bin.  Because --jitless never writes JIT code, it
#     avoids the crash entirely — which is the tell-tale sign.
#
#  2. ThinLTO (enabled implicitly by is_official_build=true)
#     The presence of fix-mksnapshot-clang20-miscompile.patch in the recipe
#     confirms Clang 20 is the compiler.  Clang 20 has a known ARM64
#     miscompilation with std::vector find+erase under LTO optimisation; the
#     patch fixes it for mksnapshot (build-time) but the same compiler bug
#     can silently corrupt runtime V8 JIT infrastructure code.
#
# Debian Bookworm's Chromium uses is_official_build=false, which disables
# both the sandbox and ThinLTO.  It works on identical hardware without any
# --js-flags workarounds.
#
# Fixes applied
# ─────────────
#  1. is_official_build=false   — matches Debian; disables sandbox + ThinLTO
#  2. v8_enable_sandbox=false   — explicit guard in case a future Chromium
#                                  version re-enables it independently
#  3. v8_enable_maglev=false    — Maglev (mid-tier JS JIT) was enabled on
#                                  ARM64 in Chromium ~130.  Its codegen is
#                                  relatively new and untested on ARMv8.0-A
#                                  Cortex-A7x cores.  Disable for stability.
#                                  This has no effect on WASM performance.
#  4. use_thin_lto=false        — belt-and-suspenders; LTO is also disabled
#                                  when is_official_build=false but we make
#                                  it explicit to survive future recipe changes.
#
# Impact on binary size / performance
# ────────────────────────────────────
# Without LTO and official-build optimisations the Chromium binary will be
# slightly larger (~10-15 %) and page-load benchmarks will be ~5-10 % slower.
# For a kiosk / IoT device displaying a fixed dashboard this is irrelevant.
# WhatsApp Web WASM crypto will now JIT-compile correctly and run at full
# Liftoff / TurboFan speed — no --js-flags workarounds needed.

# ── 1. Switch off official build ────────────────────────────────────────────
# The base recipe sets both flags in one string; we remove it and re-add only
# is_debug=false.
GN_ARGS:remove = "is_debug=false is_official_build=true"
GN_ARGS += "is_debug=false is_official_build=false"

# ── 2. Explicitly disable the V8 sandbox ────────────────────────────────────
GN_ARGS += "v8_enable_sandbox=false"

# ── 3. Disable Maglev on aarch64 ────────────────────────────────────────────
GN_ARGS:append:aarch64 = " v8_enable_maglev=false"

# ── 4. Disable ThinLTO explicitly ───────────────────────────────────────────
GN_ARGS += "use_thin_lto=false"
