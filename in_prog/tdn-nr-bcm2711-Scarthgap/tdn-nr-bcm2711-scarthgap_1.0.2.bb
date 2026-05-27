SUMMARY = "TDN Extras"
DESCRIPTION = "TDN-Eth Node-RED bundle. Mutually exclusive with node-red-contrib-tdn-whatsapp."
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

RDEPENDS:${PN} = "tdn-procscan"

inherit allarch

do_install() {
    install -d ${D}/root/app
    cp -R ${MY_FILES}/* ${D}/root/app/
}

FILES:${PN} = "/root/app"

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        hostnamectl set-hostname "TDN-Eth-Dual-V3"
        chmod +x /usr/bin/procscan
        useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
        timedatectl set-ntp false

        mkdir -p /mnt/usbStick/
        cp -v /root/app/mount-usb.sh /usr/bin/usbStick
        chmod +x /usr/bin/usbStick

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

        npm --prefix /root/install install /root/app/tdn-eth-dual_v3.2.8.tgz
        rm -rf /root/.node-red/
        mv /root/install/node_modules/TDN-Eth-Dual_v3.2.8/ /root/.node-red/
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
