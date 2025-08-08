SUMMARY = "TDN kiosk package group (Epiphany + X11 + sandbox)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

RDEPENDS:${PN} = "\
    xserver-xorg \
    xinit \
    matchbox-wm \
    xdotool \
    epiphany \
    libegl-mesa \
    libgles2-mesa \
    bubblewrap \
    xdg-dbus-proxy \
    libseccomp \
    tdn-kiosk-users \
    kiosk-session \
    xserver-nodm-init \
"
