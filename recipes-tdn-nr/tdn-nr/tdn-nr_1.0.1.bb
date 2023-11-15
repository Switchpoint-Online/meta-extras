SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale \
           file://procscan.c \
           file://gpioScan.c"

SRC_URI[sha256sum] = "5bce76250f8d9b257ef202c79a27967157b3b28f02a2b8300f964379ddb9aa50"

SRCREV = "296b927680ce7f1bbc01b0b378ecc4b2267d88f5"

S = "${WORKDIR}"

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/procscan.c -o procscan
        # ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/gpioScan.c -o gpioScan
}

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
    install -m 0755 -d ${D}${bindir} ${D}${docdir}/procscan
    install -m 0644 ${S}/procscan ${D}${bindir}
    # install -m 0755 -d ${D}${bindir} ${D}${docdir}/gpioScan
    # install -m 0644 ${S}/gpioScan ${D}${bindir}
    # install -m 0644 ${WORKDIR}/README.md ${D}${docdir}/procscan
}

FILES:${PN} = "/home/root/.node-red"
FILES:${PN} += "/usr/bin"
