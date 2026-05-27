FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://xserver-nodm \
                  file://xserver-nodm-after-nodered.conf"

do_install:append() {
    install -d ${D}/etc/default
    install -m 0644 ${WORKDIR}/xserver-nodm ${D}/etc/default/xserver-nodm

    # Ordering drop-in: start X (and thus kiosk) after node-red.service
    install -d ${D}/etc/systemd/system/xserver-nodm.service.d
    install -m 0644 ${WORKDIR}/xserver-nodm-after-nodered.conf \
        ${D}/etc/systemd/system/xserver-nodm.service.d/after-nodered.conf
}

FILES:${PN}:append = " /etc/systemd/system/xserver-nodm.service.d"
