DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=014f1a23c3da49aa929b21a96808ab22"

inherit npm

PR = "r0"

SRC_URI = "\
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
    file://${BPN}.service \
"

S = "${WORKDIR}/npm"

do_install_append() {
    # Service
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/${BPN}.service ${D}${systemd_unitdir}/system/

    # Remove hardware specific files
    rm ${D}/${bindir}/${BPN}-pi
    rm -rf ${D}/${libdir}/node_modules/${BPN}/bin
}

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "${BPN}.service"

FILES_${PN} += "\
    ${systemd_unitdir} \
"
