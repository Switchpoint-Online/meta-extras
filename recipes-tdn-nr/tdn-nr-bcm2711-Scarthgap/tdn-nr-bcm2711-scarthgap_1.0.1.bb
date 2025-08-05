SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone \
           file://procscan.c"

SRC_URI[sha256sum] = "f1a55e2d27828179cbfeb2435da245a8575f34ab8c14352d018e7405fe590329"

SRCREV = "91a3ace8812af082db79865e8ee9ff1d5a664bf8"

S = "${WORKDIR}"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -d ${D}/root
    mkdir ${D}/root/app
    cp -R ${MY_FILES}/* ${D}/root/app
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    # install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
}

FILES:${PN} = "/root/app"
FILES:${PN} += "/usr/bin"
