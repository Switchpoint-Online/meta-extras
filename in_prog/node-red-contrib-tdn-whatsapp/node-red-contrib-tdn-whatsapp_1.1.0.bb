SUMMARY = "TDN EWS + WhatsApp Node-RED bundle (TDN-EWSv3)"
DESCRIPTION = "Early Warning System with WhatsApp messaging. Mutually exclusive with tdn-nr-bcm2711-scarthgap."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

MY_FILES = "${THISDIR}/nr-files"

RDEPENDS:${PN} = "nodejs node-red chromium-x11 tdn-procscan"

inherit allarch

do_install() {
    # EWS npm bundle + app files — preserves app/ subdirectory for postinst paths
    install -d ${D}/root/app
    install -m 0644 ${MY_FILES}/tdn-ewsv3-3.0.1.tgz ${D}/root/app/
    cp -R ${MY_FILES}/app ${D}/root/app/

    # Kiosk X session scripts
    install -d ${D}${bindir}
    install -m 0755 ${MY_FILES}/x-session-manager ${D}${bindir}/x-session-manager
    install -m 0755 ${MY_FILES}/mini-x-session ${D}${bindir}/mini-x-session

    # WhatsApp session storage
    install -d ${D}/var/lib/tdn-whatsapp
}

FILES:${PN} = " \
    /root/app \
    ${bindir}/x-session-manager \
    ${bindir}/mini-x-session \
    /var/lib/tdn-whatsapp \
"

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        hostnamectl set-hostname "TDN-EWS-V3"
        chmod +x /usr/bin/procscan

        echo "dtoverlay=disable-bt" >> /boot/config.txt
        systemctl disable hciuart.service || true
        systemctl disable bluealsa.service || true
        systemctl disable bluetooth.service || true
        systemctl mask NetworkManager.service || true
        systemctl mask networking.service || true
        systemctl enable systemd-networkd.service || true
        systemctl enable systemd-resolved.service || true

        mkdir -p /etc/systemd/network

        cat <<'NETDEV' > /etc/systemd/network/10-br0.netdev
[NetDev]
Name=br0
Kind=bridge
NETDEV

        cat <<'NETCFG' > /etc/systemd/network/10-br0.network
[Match]
Name=br0

[Network]
DHCP=yes
Address=192.168.0.20/24
Address=150.150.11.4/16
Gateway=192.168.0.1
DNS=192.168.0.1
NETCFG

        cat <<'ETH0' > /etc/systemd/network/10-eth0.network
[Match]
Name=eth0

[Network]
Bridge=br0
ETH0

        cat <<'ETH1' > /etc/systemd/network/10-eth1.network
[Match]
Name=eth1

[Network]
Bridge=br0
ETH1

        mv -v /root/app/app/SHA ~/.SHA

        npm --prefix /root/install install /root/app/tdn-ewsv3-3.0.1.tgz
        rm -rf /root/.node-red/
        mv /root/install/node_modules/ /root/.node-red/
        cp -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/

        cp /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js \
           /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.bak
        sed -i 's|^\(\s*\)let digestCreds = this\.credentials;|\1var digestUser = msg.digestUser;\n\1var digestPass = msg.digestPass;\n\1let digestCreds = {"user":digestUser,"password":digestPass};|' \
           /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js

        cp /boot/cmdline.txt /boot/cmdline.bak
        sed -i 's/console=serial0,115200/console=tty99/' /boot/cmdline.txt

        reboot
    fi
}
