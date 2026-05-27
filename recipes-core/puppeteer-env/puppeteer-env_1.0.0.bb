SUMMARY = "Set PUPPETEER_SKIP_DOWNLOAD system-wide and in the Node-RED service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

# Belt-and-suspenders: set via profile.d (login/SSH shells) AND a systemd
# drop-in (node-red service) so npm/puppeteer never attempts to download
# x86 Chrome on ARM regardless of how it is invoked.
#
# NOTE: /etc/environment is owned by pam-plugin-env — installing a file
# there from a separate recipe causes an opkg file-clash (exit 255).
# Use /etc/profile.d instead, which is owned by no base package.

do_install() {
    # Login/SSH shells — covers manual npm calls, cron, su sessions
    install -d ${D}/etc/profile.d
    printf '# Prevent Puppeteer from downloading x86-only Chrome on ARM\nexport PUPPETEER_SKIP_DOWNLOAD=true\n' \
        > ${D}/etc/profile.d/puppeteer.sh

    # Systemd drop-in — ensures the node-red service itself has the variable
    install -d ${D}/etc/systemd/system/node-red.service.d
    cat > ${D}/etc/systemd/system/node-red.service.d/puppeteer.conf <<'EOF'
[Service]
Environment=PUPPETEER_SKIP_DOWNLOAD=true
EOF
}

FILES:${PN} = " \
    /etc/profile.d/puppeteer.sh \
    /etc/systemd/system/node-red.service.d/puppeteer.conf \
"
