SUMMARY = "TDN kiosk package group (Chromium + X11 + sandbox)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

# matchbox-wm intentionally excluded: Chromium --kiosk handles fullscreen
# natively and matchbox overrides --window-position on all X windows,
# which breaks the off-screen WhatsApp Chromium instance.
# libgbm, libegl-mesa, libgles2-mesa excluded: package names are renamed to
# versioned variants (libgbm1 etc.) during packaging — forbidden in packagegroups.
# They are pulled in via IMAGE_INSTALL in local.conf.
RDEPENDS:${PN} = "\
    xserver-xorg \
    xinit \
    chromium-x11 \
    bubblewrap \
    xdg-dbus-proxy \
    libseccomp \
    tdn-kiosk-users \
    xserver-nodm-init \
    kiosk-session \
"
