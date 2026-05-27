SUMMARY = "TDN systemd hardware watchdog configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

do_install() {
    install -d ${D}/etc/systemd/system.conf.d
    cat > ${D}/etc/systemd/system.conf.d/watchdog.conf << 'EOF'
[Manager]
RuntimeWatchdogSec=15
RebootWatchdogSec=2min
WatchdogDevice=/dev/watchdog
EOF
}

FILES:${PN} = "/etc/systemd/system.conf.d/watchdog.conf"
