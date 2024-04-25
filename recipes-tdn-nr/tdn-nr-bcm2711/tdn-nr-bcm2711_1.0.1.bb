SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone \
           file://procscan.c"

SRC_URI[sha256sum] = "9bb0a96c60968aaf54335fa8aa1a06814cbd5fa2fe5b3e37387a3e151fc99063"

SRCREV = "7ef747e99e5f4dbb17c2993209ba17f5297bf5b0"

S = "${WORKDIR}"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/app
    cp -R ${MY_FILES}/* ${D}/home/root/app
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    # install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
}

FILES:${PN} = "/home/root/app"
FILES:${PN} += "/usr/bin"
