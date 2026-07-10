FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Replace the default Poky logo with the TDN-OS logo.
# TDN-OS-img.h and psplash-bar-img.h are pre-generated C headers produced by
# make-image-header.sh from TDN-OS.png and the bar PNG in
# meta-extras/in_prog/tdn-nr-ews-chromium/files/base-images/.
SPLASH_IMAGES = "file://TDN-OS-img.h;outsuffix=tdn \
                 file://psplash-bar-img.h;outsuffix=bar"

SRC_URI:append = " \
    file://psplash-colors.h \
    file://psplash-config.h \
"

# psplash-colors.h and psplash-config.h live in ${S} and are compiled in.
# Copy our versions before autotools configure so they override the defaults.
do_configure:prepend() {
    cp ${WORKDIR}/psplash-colors.h ${S}/psplash-colors.h
    cp ${WORKDIR}/psplash-config.h ${S}/psplash-config.h
}
