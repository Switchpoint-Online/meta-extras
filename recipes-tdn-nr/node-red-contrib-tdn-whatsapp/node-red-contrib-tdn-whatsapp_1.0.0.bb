SUMMARY = "TDN WhatsApp node for Node-RED"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Source from your local meta-extras — no internet fetch needed
SRC_URI = " \
    file://tdn-whatsapp.js \
    file://tdn-whatsapp.html \
    file://package.json \
"

S = "${WORKDIR}"

# Node-RED must be present at install time
RDEPENDS:${PN} = "nodejs node-red"

# Chromium headless is the runtime browser dependency
RDEPENDS:${PN} += "chromium"

inherit allarch

do_install() {
    # Install package into node_modules alongside Node-RED
    install -d ${D}/usr/lib/node_modules/node-red-contrib-tdn-whatsapp

    install -m 0644 ${WORKDIR}/package.json \
        ${D}/usr/lib/node_modules/node-red-contrib-tdn-whatsapp/package.json

    install -m 0644 ${WORKDIR}/tdn-whatsapp.js \
        ${D}/usr/lib/node_modules/node-red-contrib-tdn-whatsapp/tdn-whatsapp.js

    install -m 0644 ${WORKDIR}/tdn-whatsapp.html \
        ${D}/usr/lib/node_modules/node-red-contrib-tdn-whatsapp/tdn-whatsapp.html

    # Session storage directory for WhatsApp auth tokens
    install -d ${D}/var/lib/tdn-whatsapp
}

FILES:${PN} = " \
    /usr/lib/node_modules/node-red-contrib-tdn-whatsapp \
    /var/lib/tdn-whatsapp \
"

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        # Install npm dependencies at first boot
        cd /usr/lib/node_modules/node-red-contrib-tdn-whatsapp
        PUPPETEER_SKIP_DOWNLOAD=true npm install --omit=optional \
            --prefer-offline 2>/dev/null || true

        # Link into Node-RED's node_modules so it discovers the package
        ln -sf /usr/lib/node_modules/node-red-contrib-tdn-whatsapp \
            /usr/lib/node_modules/node-red/node_modules/node-red-contrib-tdn-whatsapp \
            2>/dev/null || true

        # Set correct permissions on session dir
        chown -R admin:admin /var/lib/tdn-whatsapp || true
    fi
}