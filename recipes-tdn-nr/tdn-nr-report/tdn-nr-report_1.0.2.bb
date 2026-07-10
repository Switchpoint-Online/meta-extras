SUMMARY = "TDN Report Node-RED node (Eth only)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://letis009-node-red-contrib-tdn-report-1.0.2.tgz;unpack=false \
           file://ftp.conf \
"

S = "${WORKDIR}"

DEPENDS = "nodejs-native"
RDEPENDS:${PN} = "nodejs node-red tdn-nodered-base vsftpd"

inherit useradd

# numeronsrv: FTP-only system user, chrooted to /tmp/ftp (created at boot by tmpfiles.d)
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-r -d /tmp/ftp -s /usr/sbin/nologin numeronsrv"
EXTRA_USERS_PARAMS = "usermod -p '\$5\$XVFDS/78Rmf/n4ib\$jhYSkdnxPegl1wDVfQBmVJIG9aQ88FzBhk5cGXpPAn0' numeronsrv;"

do_compile() {
    export PUPPETEER_SKIP_DOWNLOAD=true
    export npm_config_cache="${WORKDIR}/npm-cache"
    npm --prefix ${WORKDIR}/install install ${WORKDIR}/letis009-node-red-contrib-tdn-report-1.0.2.tgz
}

do_install() {
    install -d ${D}/usr/share/tdn-nr-report
    cp -r ${WORKDIR}/install/node_modules ${D}/usr/share/tdn-nr-report/

    # tmpfiles.d — creates /tmp/ftp and /tmp/ftp/incoming on every boot
    install -d ${D}/usr/lib/tmpfiles.d
    install -m 0644 ${WORKDIR}/ftp.conf ${D}/usr/lib/tmpfiles.d/ftp.conf
}

FILES:${PN} = " \
    /usr/share/tdn-nr-report \
    /usr/lib/tmpfiles.d/ftp.conf \
"
