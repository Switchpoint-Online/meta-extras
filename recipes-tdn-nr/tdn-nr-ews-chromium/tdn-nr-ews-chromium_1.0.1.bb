SUMMARY = "TDN Extras"
DESCRIPTION = "TDN Custom Psplash Image"
LICENSE = "CLOSED"

LIC_FILES_CHKSUM = ""

# Replace this with your actual relative path if different
MY_FILES = "${THISDIR}/files"

SRC_URI = " \
    file://psplash.c \
    file://psplash.h \
    file://psplash-colors.h \
    file://psplash_TDN-POD.h \
"

S = "${WORKDIR}"

do_compile() {
    oe_runmake 'CC=${CC}' \
               'CFLAGS=${CFLAGS}' \
               'LDFLAGS=${LDFLAGS}' \
               'PSPLASH_IMG=psplash_TDN-POD.h' \
               psplash
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/psplash ${D}${bindir}/psplash
}

FILES:${PN} += "${bindir}/psplash"
