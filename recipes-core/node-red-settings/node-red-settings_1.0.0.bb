SUMMARY = "TDN Node-RED settings.js — configures userDir and nodesDir for TDN packages"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://settings.js"

S = "${WORKDIR}"

inherit allarch

RDEPENDS:${PN} = "node-red"

do_install() {
    # Create /root and /root/.node-red with correct ownership/permissions.
    # base-files does not create /root in this distro configuration,
    # so we own the directory here.
    install -d -m 0700 ${D}/root
    install -d -m 0700 ${D}/root/.node-red

    install -m 0644 ${WORKDIR}/settings.js ${D}/root/.node-red/settings.js
}

FILES:${PN} = " \
    /root \
    /root/.node-red \
    /root/.node-red/settings.js \
"
