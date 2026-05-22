SUMMARY = "Set PUPPETEER_SKIP_DOWNLOAD system-wide and in the Node-RED service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

# Belt-and-suspenders: set in /etc/environment (PAM/shell sessions) AND
# as a systemd drop-in so the node-red service inherits it even without PAM.
# This prevents npm/puppeteer from attempting to download x86 Chrome on ARM.

do_install() {
    # Global environment — covers SSH sessions, cron, any manual npm calls
    install -d ${D}/etc
    echo "PUPPETEER_SKIP_DOWNLOAD=true" >> ${D}/etc/environment

    # Systemd drop-in — ensures the node-red service itself has the variable
    install -d ${D}/etc/systemd/system/node-red.service.d
    cat > ${D}/etc/systemd/system/node-red.service.d/puppeteer.conf <<'EOF'
[Service]
Environment=PUPPETEER_SKIP_DOWNLOAD=true
EOF
}

FILES:${PN} = " \
    /etc/environment \
    /etc/systemd/system/node-red.service.d/puppeteer.conf \
"
