SUMMARY = "Minimal X session that runs Chromium in kiosk mode"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://xsession-kiosk \
           file://tdn-kiosk"

S = "${WORKDIR}"

RDEPENDS:${PN} = "chromium-x11 xinit xserver-nodm-init"

do_install() {
    install -d ${D}/etc/default
    install -m 0644 ${WORKDIR}/tdn-kiosk ${D}/etc/default/tdn-kiosk

    # Primary session path: xserver-nodm.service runs as User=root so Xsession
    # checks $HOME/.Xsession = /root/.Xsession (capital X, 90XWindowManager.sh).
    install -d -m 0700 ${D}/root
    install -m 0755 ${WORKDIR}/xsession-kiosk ${D}/root/.Xsession

    # Secondary path: kept for future migration to rootless X (kiosk user).
    install -d ${D}/home/kiosk
    install -m 0755 ${WORKDIR}/xsession-kiosk ${D}/home/kiosk/.xsession
}

FILES:${PN} += " \
  /etc/default/tdn-kiosk \
  /root/.Xsession \
  /home/kiosk/.xsession \
"
