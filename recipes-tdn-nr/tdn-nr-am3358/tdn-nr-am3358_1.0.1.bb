SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale \
           file://procscan.c"

SRC_URI[sha256sum] = "dfa511bb18139a375f77f5fbd87e97d6daf1d52749dfdb5111ee06cd0167f8a1"

SRCREV = "926a98008f981fb76234e09409b9a261d8988ce6"

S = "${WORKDIR}"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
}

FILES:${PN} = "/home/root/.node-red"
FILES:${PN} += "/usr/bin"
