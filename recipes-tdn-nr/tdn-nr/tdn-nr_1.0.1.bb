SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale \
           file://procscan.c \
           file://README.md"

SRC_URI[sha256sum] = "5bce76250f8d9b257ef202c79a27967157b3b28f02a2b8300f964379ddb9aa50"

SRCREV = "f36c3ac36b1bb522ea9f58302c4d391d2d43270e"

S = "${WORKDIR}"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
}

do_install() {
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
}

FILES:${PN}= "/home/root/.node-red"
