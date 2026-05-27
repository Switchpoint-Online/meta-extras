SUMMARY = "TDN WhatsApp Node-RED node (EWS only)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://node-red-contrib-tdn-whatsapp-1.0.1.tgz;unpack=false"

S = "${WORKDIR}"

DEPENDS = "nodejs-native"
RDEPENDS:${PN} = "nodejs node-red tdn-nodered-base chromium-x11 puppeteer-env"

# bare-url/bare-fs/bare-os ship prebuilt linux-arm64 native addons (.bare files)
# that link against libc. libc is always present in the base image — skip the
# file-rdeps QA check rather than listing glibc explicitly in RDEPENDS.
INSANE_SKIP:${PN} = "file-rdeps"

# npm downloads whatsapp-web.js transitive deps (fluent-ffmpeg etc.) from registry.
do_compile[network] = "1"

do_compile() {
    export PUPPETEER_SKIP_DOWNLOAD=true
    export npm_config_cache="${WORKDIR}/npm-cache"
    npm --prefix ${WORKDIR}/install install ${WORKDIR}/node-red-contrib-tdn-whatsapp-1.0.1.tgz

    # Patch the dev-machine fallback path to the correct on-image path.
    # The dev default (/home/admin/wa-alarm/.wwebjs_auth) does not exist on
    # the Yocto image and cannot be fixed without a reflash — there is no SSH.
    # /data/tdn-whatsapp is created by do_install() below and is always present.
    NR_NODE="${WORKDIR}/install/node_modules/node-red-contrib-tdn-whatsapp"
    sed -i "s|/home/admin/wa-alarm/.wwebjs_auth|/data/tdn-whatsapp|g" \
        "${NR_NODE}/tdn-whatsapp.js"
    sed -i "s|/home/admin/wa-alarm/.wwebjs_auth|/data/tdn-whatsapp|g" \
        "${NR_NODE}/tdn-whatsapp.html"

    # --disable-gpu causes a SIGSEGV (signal 11) in chromium-bin on this Yocto
    # build.  chromium-x11 is compiled with the x11 Ozone backend and uses
    # ANGLE/EGL for compositing — the GPU process is required.  --disable-gpu
    # is safe on Raspberry Pi OS (different rendering path) but fatal here.
    # --disable-accelerated-2d-canvas is also removed: unnecessary with GPU
    # enabled and absent from the kiosk Chromium which runs without issues.
    sed -i "/'--disable-gpu',/d" \
        "${NR_NODE}/tdn-whatsapp.js"
    sed -i "/'--disable-accelerated-2d-canvas',/d" \
        "${NR_NODE}/tdn-whatsapp.js"

    # chromium-x11 is compiled with the x11 Ozone backend only — the headless
    # Ozone backend is not present.  Puppeteer with headless:true internally
    # prepends --ozone-platform=headless to Chromium's argv; Chromium honours
    # the FIRST occurrence, so any --ozone-platform=x11 we append is ignored.
    #
    # Fix: set headless:false so Puppeteer never injects the headless flag.
    # Node-RED runs with DISPLAY=:0 (node-red-override.conf) so Chromium will
    # connect to the Xorg server on :0 using the x11 backend.
    # --window-position=-10000,-10000 keeps the WA browser window off-screen
    # (the DSI panel is 800×480 — a window at −10000,−10000 is never visible).
    sed -i "s/headless: true,/headless: false,/" \
        "${NR_NODE}/tdn-whatsapp.js"
    sed -i "s|'--mute-audio',|'--mute-audio',\n                '--window-position=-10000,-10000',\n                '--ozone-platform=x11',|" \
        "${NR_NODE}/tdn-whatsapp.js"
    # --single-process crashes Chromium when WA Web navigates after QR auth.
    sed -i "/single-process/d" \
        "${NR_NODE}/tdn-whatsapp.js"
    # --no-zygote causes SIGSEGV when WhatsApp Web navigates from the QR page
    # to the main chat interface.  That navigation spawns a new renderer process;
    # without the zygote, Chromium uses direct fork+exec which crashes on this
    # ARM64 build.  --no-sandbox is already set (running as root) so the zygote
    # sandbox is not needed — the zygote itself is safe to keep.
    sed -i "/'--no-zygote',/d" \
        "${NR_NODE}/tdn-whatsapp.js"

    # Move QR page from httpAdmin (password-protected /adminNodes/whatsapp-qr)
    # to httpNode (open, /whatsapp-qr).  httpNodeRoot='/' so the URL is simply
    # http://<device-ip>/whatsapp-qr — accessible from any browser without login.
    sed -i "s|RED\.httpAdmin\.get('/whatsapp-qr', RED\.auth\.needsPermission('flows\.read'),|RED.httpNode.get('/whatsapp-qr',|" \
        "${NR_NODE}/tdn-whatsapp.js"

    # Chromium throttles JS in off-screen windows (--window-position=-10000,-10000)
    # causing Puppeteer CDP calls to time out ("Runtime.callFunctionOn timed out").
    # Disable background throttling so whatsapp-web.js initialisation completes.
    sed -i "s|'--ozone-platform=x11',|'--ozone-platform=x11',\n                '--disable-background-timer-throttling',\n                '--disable-renderer-backgrounding',\n                '--disable-backgrounding-occluded-windows',|" \
        "${NR_NODE}/tdn-whatsapp.js"

    # Increase Puppeteer protocol timeout from default 180 s to 300 s.
    # Pi hardware is slow; whatsapp-web.js initialization JS can exceed 3 min.
    sed -i "s/headless: false,/headless: false,\n                protocolTimeout: 300000,/" \
        "${NR_NODE}/tdn-whatsapp.js"
}

do_install() {
    install -d ${D}/usr/share/tdn-nr-whatsapp
    cp -r ${WORKDIR}/install/node_modules ${D}/usr/share/tdn-nr-whatsapp/

    install -d ${D}/data/tdn-whatsapp

    # Remove prebuilt native binaries for non-AArch64 platforms.
    # Packages like bare-url/bare-fs/bare-os ship prebuilds/ for every
    # platform (linux-x64, android-arm, etc.). Yocto QA fails on foreign
    # ELF binaries. Only linux-arm64 is needed on this target.
    find ${D}/usr/share/tdn-nr-whatsapp -type d -name "prebuilds" | while IFS= read -r dir; do
        find "$dir" -mindepth 1 -maxdepth 1 -type d ! -name "linux-arm64" -exec rm -rf {} +
    done
}

FILES:${PN} = " \
    /usr/share/tdn-nr-whatsapp \
    /data/tdn-whatsapp \
"
