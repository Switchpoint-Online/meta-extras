SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone \
           file://procscan.c"

SRC_URI[sha256sum] = "0cd7aefd3cf592e82c875bde9341a92f6532dbef27d3f0cc96cf25be57c24264"

SRCREV = "6ee0e0576700646ee758ceb2724768fe33bf0f49"

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
