SUMMARY = "Minimal X session that runs Epiphany in kiosk mode"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://xsession-kiosk \
           file://tdn-kiosk"

S = "${WORKDIR}"

RDEPENDS:${PN} = "epiphany matchbox-wm xinit xserver-nodm-init"

do_install() {
    install -d ${D}/etc/default
    install -m 0644 ${WORKDIR}/tdn-kiosk ${D}/etc/default/tdn-kiosk
    install -d ${D}/home/kiosk
    install -m 0755 ${WORKDIR}/xsession-kiosk ${D}/home/kiosk/.xsession
}

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        if id kiosk >/dev/null 2>&1; then
            chown kiosk:kiosk /home/kiosk/.xsession || true
        fi
    fi
}

FILES:${PN} += " \
  /etc/default/xserver-nodm \
  /etc/default/tdn-kiosk \
  /home/kiosk/.xsession \
"
