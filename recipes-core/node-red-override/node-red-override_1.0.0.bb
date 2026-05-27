SUMMARY = "TDN Node-RED systemd service hardening — restart policy and memory limits"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

RDEPENDS:${PN} = "node-red"

do_install() {
    install -d ${D}/etc/systemd/system/node-red.service.d
    cat > ${D}/etc/systemd/system/node-red.service.d/tdn-override.conf << 'EOF'
[Unit]
# Ensure Node-RED starts after the network stack is ready.
After=network.target

[Service]
Restart=always
RestartSec=5
StartLimitIntervalSec=0
MemoryMax=512M
MemorySwapMax=0
# Run node directly (service already runs as root — no need for 'su root -c').
# This guarantees Environment= vars below are inherited by the Node-RED process.
ExecStart=
ExecStart=/usr/bin/node /usr/lib/node_modules/node-red/red.js
Environment=HOME=/root
Environment=PUPPETEER_SKIP_DOWNLOAD=true
Environment=NODE_OPTIONS=--max-old-space-size=256
# Chromium (Puppeteer/whatsapp-web.js) needs DISPLAY for the x11 Ozone backend.
# Xorg is started by xserver-nodm on :0 before Node-RED reaches 'ready'.
Environment=DISPLAY=:0
EOF
}

FILES:${PN} = "/etc/systemd/system/node-red.service.d/tdn-override.conf"
