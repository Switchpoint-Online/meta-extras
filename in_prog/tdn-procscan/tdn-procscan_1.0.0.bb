SUMMARY = "TDN process hash scanner — shared utility for all TDN Node-RED projects"
LICENSE = "CLOSED"

SRC_URI = "file://procscan.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 ${S}/procscan ${D}${bindir}
}

FILES:${PN} = "${bindir}/procscan"
