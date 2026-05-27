SUMMARY = "TDN Dahua RPC2 Node-RED node (Eth only)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://node-red-contrib-dahua-rpc2-1.0.2.tgz;unpack=false"

S = "${WORKDIR}"

DEPENDS = "nodejs-native"
RDEPENDS:${PN} = "nodejs node-red tdn-nodered-base"

do_compile() {
    export PUPPETEER_SKIP_DOWNLOAD=true
    export npm_config_cache="${WORKDIR}/npm-cache"
    npm --prefix ${WORKDIR}/install install ${WORKDIR}/node-red-contrib-dahua-rpc2-1.0.2.tgz
}

do_install() {
    install -d ${D}/usr/share/tdn-nr-dahua
    cp -r ${WORKDIR}/install/node_modules ${D}/usr/share/tdn-nr-dahua/
}

FILES:${PN} = "/usr/share/tdn-nr-dahua"
