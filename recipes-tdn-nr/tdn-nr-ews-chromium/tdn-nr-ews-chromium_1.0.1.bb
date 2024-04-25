SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone \
           file://procscan.c"

SRC_URI[sha256sum] = "cfa03b0ea4778b0d5057b0f6e7ea8b45e3cb9585ea99445d309a4a8a3e26e3be"

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
