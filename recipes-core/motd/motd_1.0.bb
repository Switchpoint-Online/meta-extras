SUMMARY = "Custom Message of the Day"
DESCRIPTION = "Sets a custom Message of the Day for TDN-OS."
LICENSE = "CLOSED"
PR = "r0"

SRC_URI = "file://motd"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/motd ${D}${sysconfdir}/motd
}
