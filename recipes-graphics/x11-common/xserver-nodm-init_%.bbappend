FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://xserver-nodm"

do_install:append() {
    install -d ${D}/etc/default
    install -m 0644 ${WORKDIR}/xserver-nodm ${D}/etc/default/xserver-nodm
}
