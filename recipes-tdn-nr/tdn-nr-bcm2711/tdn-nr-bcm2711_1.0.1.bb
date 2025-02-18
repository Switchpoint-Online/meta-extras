SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone \
           file://procscan.c"

SRC_URI[sha256sum] = "685ee8b2187f0e2732aa35cc7a8eccfd3b5b8ef2dcea3447bc551c0c855e63a1"

SRCREV = "98fa5408fbb7d9fc2590868796927c887c593c72"

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
