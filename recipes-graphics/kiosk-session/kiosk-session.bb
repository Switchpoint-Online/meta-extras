SUMMARY = "Minimal X session that runs Epiphany in kiosk mode"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://Xsession-kiosk \
           file://xserver-nodm.default \
           file://tdn-kiosk"

S = "${WORKDIR}"

# We need these at runtime
RDEPENDS:${PN} = "epiphany matchbox-wm xserver-nodm-init"

do_install() {
    # Xsession
    install -d ${D}/etc/X11
    install -m 0755 ${WORKDIR}/Xsession-kiosk ${D}/etc/X11/Xsession

    # xserver-nodm config
    install -d ${D}/etc/default
    install -m 0644 ${WORKDIR}/xserver-nodm.default ${D}/etc/default/xserver-nodm

    # Kiosk URL config (you can change this later on target)
    install -m 0644 ${WORKDIR}/tdn-kiosk ${D}/etc/default/tdn-kiosk
}

# Make sure the kiosk user owns its home bits after the user is created
pkg_postinst:${PN} () {
    # If running on target (not during do_rootfs), set ownership
    if [ -n "$D" ]; then
        exit 0
    fi
    if id kiosk >/dev/null 2>&1; then
        mkdir -p /home/kiosk/.local/share
        chown -R kiosk:kiosk /home/kiosk/.local || true
    fi
}

FILES:${PN} += " \
  /etc/X11/Xsession \
  /etc/default/xserver-nodm \
  /etc/default/tdn-kiosk \
"
