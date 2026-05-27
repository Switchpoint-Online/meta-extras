SUMMARY = "TDN HCP API Node-RED node (POD only)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://letis009-node-red-contrib-tdn-hcp-api-2.1.0.tgz;unpack=false"

S = "${WORKDIR}"

DEPENDS = "nodejs-native"
RDEPENDS:${PN} = "nodejs node-red tdn-nodered-base"

do_compile() {
    export PUPPETEER_SKIP_DOWNLOAD=true
    export npm_config_cache="${WORKDIR}/npm-cache"
    npm --prefix ${WORKDIR}/install install ${WORKDIR}/letis009-node-red-contrib-tdn-hcp-api-2.1.0.tgz
}

do_install() {
    install -d ${D}/usr/share/tdn-nr-hcp
    cp -r ${WORKDIR}/install/node_modules ${D}/usr/share/tdn-nr-hcp/
}

FILES:${PN} = "/usr/share/tdn-nr-hcp"
