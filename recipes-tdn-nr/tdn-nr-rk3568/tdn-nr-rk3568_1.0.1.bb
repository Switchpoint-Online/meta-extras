SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=dunfell \
           file://procscan.c \
           file://README.md"

SRC_URI[sha256sum] = "54efe9e8d8f666e4c6da46fd959f3b47b026ec212da89fffd54cd3658b39af1c"

SRCREV = "bfafda71de5c4b91bcf6ee7c6578dd50fec9edca"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
    install -d ${D}/home/root
    mkdir ${D}/home/root/app
    cp -R ${MY_FILES}/* ${D}/home/root/app
}

FILES:${PN}= "/home/root/app"
