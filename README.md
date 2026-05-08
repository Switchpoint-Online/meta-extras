# meta-extras
## Raspberry Pi Langdale BSP RPI0-2w
```cd ~/Yocto/Langdale/
git clone -b langdale git://git.yoctoproject.org/poky.git
git clone -b langdale git://git.yoctoproject.org/meta-raspberrypi.git
git clone -b langdale git://git.openembedded.org/meta-openembedded
git clone -b langdale https://github.com/intel-iot-devkit/meta-iot-cloud.git 
git clone git@github.com:Switchpoint-Online/meta-extras.git -b kirkstone
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-raspberrypi
bitbake-layers add-layer ../meta-iot-cloud/
bitbake-layers add-layer ../meta-extras/
bitbake core-image-base --runonly=fetch
bitbake core-image-base
```

## Raspberry Pi Langdale BSP RPI0-2w
```cd tmp/deploy/images/raspberrypi0-2w/
bzip2 -d -f core-image-base-raspberrypi0-2w.wic.bz2
sudo dd bs=4M if=core-image-base-raspberrypi0-2w.wic of=/dev/sde status=progress conv=fsync
cd ~/Yocto/build/
exit
```
## Raspberry Pi Langdale BSP CM4-Waveshare Nano ethernet
```cd tmp/deploy/images/raspberrypi4-64/
bzip2 -d -f core-image-base-raspberrypi4-64.wic.bz2
sudo dd status=progress conv=fsync bs=4M if=core-image-base-raspberrypi4-64.wic of=/dev/sdf
exit
```
### setup security and install GSI v3.5
#### Run as root
su root
```
hostnamectl set-hostname "TDN-GSI-V3"
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
rm -r ~/.node-red/
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.BAK
sed -i 's|                    let digestCreds = this.credentials;|                    var digestUser = msg.digestUser;\n                    var digestPass = msg.digestPass;\n                    let digestCreds = {"user":digestUser,"password":digestPass};|' /lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
timedatectl set-ntp false
```
### setup security and install FTP NODE
#### Run as root
su root
```
hostnamectl set-hostname "TDN-ETHv3"
nmcli d wifi conn B26A24 password "Rn!ug:Po(aA{;g2ATf7|UxwtkX3Q)sZ3"
nmcli c down B26A24
nmcli d wifi hotspot ifname wlan0 ssid TDN-Portal password "LOGtn63u"
nmcli connection modify Hotspot 802-11-wireless.mode ap 802-11-wireless.band bg
nmcli connection modify Hotspot wifi-sec.key-mgmt wpa-psk
nmcli connection modify Hotspot wifi-sec.psk LOGtn63u
nmcli connection modify Hotspot ipv4.method manual ipv4.addresses 192.168.4.1/24 ipv4.gateway 192.168.4.1 ipv4.dns 192.168.4.1
useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
rm -r ~/.node-red/
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
timedatectl set-ntp false
```

### Scarthgap TDN
```
mkdir TDN-Scarthgap
cd TDN-Scarthgap/
git clone -b scarthgap git://git.yoctoproject.org/poky.git
git clone -b scarthgap git://git.yoctoproject.org/meta-raspberrypi.git
git clone -b scarthgap git://git.openembedded.org/meta-openembedded
git clone -b scarthgap https://github.com/intel-iot-devkit/meta-iot-cloud.git 
git clone git@github.com:Switchpoint-Online/meta-extras.git -b kirkstone
ls -al
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-raspberrypi
bitbake-layers add-layer ../meta-iot-cloud/
bitbake-layers add-layer ../meta-extras/
bitbake-layers show-layers
bitbake core-image-base --runonly=fetch
bitbake core-image-base
cd tmp/deploy/images/raspberrypi4-64/
```
#### Base line configuration
```
hostnamectl set-hostname "TDN-GSI-V3"
chmod +x /usr/bin/procscan
useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
mv -v /root/app/app/SHA ~/.SHA
npm --prefix /root/install install /root/app/tdn-ethv3-3.1.0.tgz
rm -r ~/.node-red/
mv /root/install/node_modules/tdn-ethv3/ /root/.node-red/
cp -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/
timedatectl set-ntp false
```
#### TDN-GSI // Dual ethernet non bridged
```
hostnamectl set-hostname "TDN-Eth-V3"
chmod +x /usr/bin/procscan
useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
mv -v /root/app/app/SHA ~/.SHA
npm --prefix /root/install install /root/app/tdn-ethv3-3.1.0.tgz
rm -r ~/.node-red/
mv /root/install/node_modules/tdn-ethv3/ /root/.node-red/
cp -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/
timedatectl set-ntp false
systemctl mask NetworkManager.service
systemctl mask networking.service
systemctl enable systemd-networkd.service
systemctl enable systemd-resolved.service
cat <<EOF | tee /etc/systemd/network/10-eth0.network
[Match]
Name=eth0

[Network]
DHCP=yes
Address=192.168.0.20/24
Gateway=192.168.0.1
EOF

cat <<EOF | tee /etc/systemd/network/10-eth1.network
[Match]
Name=eth1

[Network]
Address=192.168.2.10/24
DNS=8.8.4.4
EOF
```
#### TDN-GSI Dual// Dual ethernet bridged
```hostnamectl set-hostname "TDN-Eth-Dual-V3"
chmod +x /usr/bin/procscan
su -c "useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv" root
timedatectl set-ntp false
mkdir -p /mnt/usbStick/
cp -v app/mount-usb.sh /usr/bin/usbStick
chmod +x /usr/bin/usbStick
echo "dtoverlay=disable-bt" >> /boot/config.txt
systemctl disable hciuart.service
systemctl disable bluealsa.service
systemctl disable bluetooth.service
systemctl mask NetworkManager.service
systemctl mask networking.service
systemctl enable systemd-networkd.service
systemctl enable systemd-resolved.service
cat <<EOF | tee /etc/systemd/network/10-br0.netdev
[NetDev]
Name=br0
Kind=bridge
EOF

cat <<EOF | tee /etc/systemd/network/10-br0.network
[Match]
Name=br0

[Network]
DHCP=yes
Address=192.168.0.20/24
Address=150.150.11.4/16
Gateway=192.168.0.1
DNS=192.168.0.1
EOF

cat <<EOF | tee /etc/systemd/network/10-eth0.network
[Match]
Name=eth0

[Network]
Bridge=br0
EOF

cat <<EOF | tee /etc/systemd/network/10-eth1.network
[Match]
Name=eth1

[Network]
Bridge=br0
EOF

mv -v /root/app/app/SHA ~/.SHA

npm --prefix /root/install install /root/app/tdn-eth-dual_v3.2.8.tgz

rm -r ~/.node-red/
mv /root/install/node_modules/TDN-Eth-Dual_v3.2.8/ /root/.node-red/
cp -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/
su -c "cp /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.bak && sed -i 's|^\(\s*\)let digestCreds = this\.credentials;|\1var digestUser = msg.digestUser;\n\1var digestPass = msg.digestPass;\n\1let digestCreds = {"user":digestUser,"password":digestPass};|' /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js" root
su -c "cp /boot/cmdline.txt /boot/cmdline.bak && sed -i 's/console=serial0,115200/console=tty99/' /boot/cmdline.txt" root
su -c "reboot" root
```
### wpa_supplicant -B -i wlan0 -c <(wpa_passphrase RC19_2gz 'd15_muN9@13') && udhcpc -i wlan0
```
DEST=/root/.node-red/node_modules/node-red-contrib-tdn-sts-gen2 && \
rm -rf "$DEST" && \
mkdir -p "$DEST" && \
cp -r /boot/TDN-STS_gen2/* "$DEST/" && \
cd "$DEST" && \
npm install --production && \
systemctl restart node-red
```

#### TDN-EWS 
```
hostnamectl set-hostname "TDN-EWSv2"
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
rm -r /home/root/.node-red/
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
```

### AP Configuration PRI
#### NMCLI 
```
nmcli d wifi conn B26A24 password "Rn!ug:Po(aA{;g2ATf7|UxwtkX3Q)sZ3"
nmcli c down B26A24
nmcli d wifi hotspot ifname wlan0 ssid TDN-Portal password "LOGtn63u"
nmcli connection modify Hotspot 802-11-wireless.mode ap 802-11-wireless.band bg
nmcli connection modify Hotspot wifi-sec.key-mgmt wpa-psk
nmcli connection modify Hotspot wifi-sec.psk LOGtn63u
nmcli connection modify Hotspot ipv4.method manual ipv4.addresses 192.168.4.1/24 ipv4.gateway 192.168.4.1 ipv4.dns 192.168.4.1
nmcli c up Hotspot
```

### setup security and install DiSU NODE
#### Run as root
su root
```
hostnamectl set-hostname "TDN-iFaceV4"
useradd -p $(echo r8 | openssl passwd -1 -stdin) config
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
rm -R ~/.node-red
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
```
# Clone SD Card and add Serial to DRM
```
sudo dd if=/dev/sde of=TDN-DiSU_v1.img status=progress
```


## RADXA CM3 IO Board - incl waveshare POE
### Working to test
```mkdir Yocto/ 
cd Yocto/ 
mkdir TDN-GSI-Radxa-cm3-Dunfell/
cd TDN-GSI-Radxa-cm3-Dunfell/
git clone -b dunfell git://git.openembedded.org/meta-openembedded
git clone -b dunfell git://git.yoctoproject.org/poky.git
git clone -b dunfell git://git.yoctoproject.org/meta-security.git
git clone -b dunfell git@github.com:Switchpoint-Online/meta-extras.git
git clone -b dunfell https://github.com/radxa/meta-radxa.git
git clone -b dunfell https://github.com/YoeDistro/meta-python2.git
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe/
bitbake-layers add-layer ../meta-openembedded/meta-python/
bitbake-layers add-layer ../meta-openembedded/meta-networking/
bitbake-layers add-layer ../meta-python2/
bitbake-layers add-layer ../meta-extras/
bitbake-layers add-layer ../meta-radxa/
bitbake -k radxa-console-image --runonly=fetch
bitbake -k radxa-console-image```
```
#### Install rkdeveloptool
```
git clone https://github.com/rockchip-linux/rkdeveloptool.git
add rkdeveloptool to /bin PATH
```
#### load usb drivers to rk3568 and flash img
```
sudo rkdeveloptool db ~/Yocto/Rockpi/rk356x_spl_loader_ddr1056_v1.10.111.bin
sudo rkdeveloptool wl 0x0000 ~/Yocto/TDN-GSI-Radxa-cm3-Dunfell/build/tmp/deploy/images/radxa-cm3-io-rk3566/radxa-console-image-radxa-cm3-io-rk3566-gpt.img 
sudo rkdeveloptool rd
```
### setup security and install
#### Run as root
rock 
su root
passwd root
```
cd ~/app
tar -xvf node-v16.20.1-linux-arm64.tar.xz
sudo cp -r node-v16.20.1-linux-arm64/{bin,include,lib,share} /usr/
export PATH=/usr/node-v16.20.1-linux-arm64/bin:$PATH
sudo npm install -g --unsafe-perm node-red
npm --prefix /home/root/install install /home/root/app/nr.tgz
mv /home/root/install/node_modules/node-red-project/ /home/root/.node-red/
mv -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
mv -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
mv -v node-red.service /lib/systemd/system/node-red.service
mv -v /home/root/app/app/SHA ~/.SHA
systemctl daemon-reload
systemctl enable node-red 
systemctl start node-red
```

## Beaglebone Black kirkstone BSP
```
git clone -b kirkstone git://git.openembedded.org/meta-openembedded
git clone -b kirkstone git://git.yoctoproject.org/poky.git
git clone -b kirkstone git@github.com:Switchpoint-Online/meta-extras.git
git clone -b kirkstone https://github.com/intel-iot-devkit/meta-iot-cloud.git
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-extras/
bitbake-layers add-layer ../meta-iot-cloud/
bitbake core-image-minimal --runonly=fetch
bitbake core-image-minimal
sudo dd bs=4M if=core-image-minimal-beaglebone-yocto.wic of=/dev/sde status=progress conv=fsync
```

### setup security and install
#### Run as root
su root
```
useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
mv -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
echo mv -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
mv -v /home/root/app/node-red.service /lib/systemd/system/node-red.service
timedatectl set-ntp false
systemctl daemon-reload
systemctl enable node-red 
systemctl start node-red
```

### Work in Progress - dunfell
#### TODO - dunfell
Added Nelson Robert Kernal and get Modprobe functional 
```

                    var digestUser = msg.digestUser; 
                    var digestPass = msg.digestPass;
                    let digestCreds = {"user":digestUser,"password":digestPass};
```

# AP Configuration PRI
## NMCLI 
```
nmcli d wifi conn B26A24 password "Rn!ug:Po(aA{;g2ATf7|UxwtkX3Q)sZ3"
nmcli c down B26A24
nmcli d wifi hotspot ifname wlan0 ssid TDN-Portal password "LOGtn63u"
nmcli connection modify Hotspot 802-11-wireless.mode ap 802-11-wireless.band bg
nmcli connection modify Hotspot wifi-sec.key-mgmt wpa-psk
nmcli connection modify Hotspot wifi-sec.psk LOGtn63u
nmcli connection modify Hotspot ipv4.method manual ipv4.addresses 192.168.4.1/24 ipv4.gateway 192.168.4.1 ipv4.dns 192.168.4.1
nmcli c up Hotspot


```

# RPI Disable Bluetooth and smb
/etc/samba/smb.conf
```
echo "[config]
        path = /home/config/ipdev/config/
        read only = no
        inherit permissions = yes
	    valid users = config

[update]
        path = /home/config/ipdev/update/
        read only = no
        inherit permissions = yes
    	valid users = config
" >> /etc/samba/smb.conf
```

 /boot/config.txt
# Disable Bluetooth
```
echo "dtoverlay=disable-bt" >> /boot/config.txt
```

Info: Disable onboard Bluetooth on Pi 3B, 3B+, 3A+, 4B and Zero W, restoring UART0/ttyAMA0 over GPIOs 14 (pin 8) & 15 (pin10). 
N.B. To disable the systemd service that initialises the modem so it doesn’t use the UART, use ‘sudo systemctl disable hciuart’.

Disable related services Permalink
```
systemctl disable hciuart.service
systemctl disable bluealsa.service
systemctl disable bluetooth.service
```
Reboot to apply the changes Permalink
```
PI 2 / 3
dwc_otg.lpm_enable=0 console=ttyAMA0,115200 console=tty1 root=/dev/mmcblk0p2 rootfstype=ext4 cgroup_enable=memory elevator=deadline rootwait
PIzero 2
dwc_otg.lpm_enable=0 console=ttyAMA0,115200 console=tty1 root=/dev/mmcblk0p2 rootfstype=ext4 rootwait  logo.nologo
```

Even after disabling on-board Bluetooth and related services, Bluetooth will be available when a Bluetooth adapter (e.g. Plugable Bluetooth Adapter) is plugged in.

systemctl disable serial-getty@ttyAMA0.service

## add 7inch DSI
```
# Enable VC4 Graphics
dtoverlay=vc4-kms-dsi-7inch
#dtoverlay=disable-bt
dtoverlay=dwc2,dr_mode=host
```
/usr/bin/mini-x-session


## nmcli
nmcli c mod 'Wired connection 1' ipv4.method manual ipv4.addr "172.14.14.14/24"


# meta-extras
## Raspberry Pi Yocto 5 Scarthgap
```cd ~/Yocto/TDN-Scarthgap/
git clone -b scarthgap git://git.yoctoproject.org/poky.git
git clone -b scarthgap git://git.yoctoproject.org/meta-raspberrypi.git
git clone -b scarthgap git://git.openembedded.org/meta-openembedded
git clone -b scarthgap https://github.com/intel-iot-devkit/meta-iot-cloud.git 
git clone git@github.com:Switchpoint-Online/meta-extras.git -b kirkstone
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-raspberrypi
bitbake-layers add-layer ../meta-iot-cloud/
bitbake-layers add-layer ../meta-extras/
bitbake core-image-base --runonly=fetch
bitbake core-image-base
```

# BUILD USB detection support 
```
IMAGE_INSTALL:append = " \
  nodejs \
  util-linux \
  usbutils \
  libudev \
  e2fsprogs \
  dosfstools \
"
```

### CM5 Kiosk Scarthgap - built meta-extras with package_group
```
dwc_otg.lpm_enable=0 console=ttyAMA0,115200 root=/dev/mmcblk0p2 rootfstype=ext4 rootwait  logo.nologo net.ifnames=0 video=SimpleDRM:off


enable_uart=1
enable_rp1_uart=1
dtparam=uart0=on
pciex4_reset=0

#dtoverlay=act-led,activelow=off
# Enable VC4 Graphics
# dtoverlay=vc4-kms-v3d
dtoverlay=vc4-kms-v3d-pi5

# Enable USB peripheral mode
dtoverlay=dwc2,dr_mode=peripheral

mkdir -p /etc/X11/xorg.conf.d
tee /etc/X11/xorg.conf.d/20-modesetting.conf <<'EOF'
Section "Device"
    Identifier "VC4-KMS"
    Driver     "modesetting"
    Option     "AccelMethod" "glamor"
    Option     "PrimaryGPU" "true"
    Option     "kmsdev" "/dev/dri/card0"
EndSection
EOF
tee /etc/default/xserver-nodm <<'EOF'
ENABLED=yes
USER=root
DISPLAY=:0
XSERVER=/usr/bin/Xorg
XSERVER_OPTIONS="-verbose -logverbose 6 -keeptty vt1 -configdir /etc/X11/xorg.conf.d"
XINIT=/usr/bin/x-session-manager
EOF
tee /etc/X11/Xsession.d/20-display-power <<'EOF'
#!/bin/sh
xset s off -dpms s noblank || true
EOF
chmod +x /etc/X11/Xsession.d/20-display-power
tee /usr/bin/x-session-manager <<'EOF'
#!/bin/sh
#
# Very simple session manager for Mini X
#

# Uncomment below to enable parsing of debian menu entrys
# export MB_USE_DEB_MENUS=1 

if [ -e $HOME/.mini_x/session ]
then
exec $HOME/.mini_x/session
fi

if [ -e /etc/mini_x/session ]
then
exec /etc/mini_x/session
fi

MINI_X_SESSION_DIR=/etc/mini_x/session.d
if [ -d "$MINI_X_SESSION_DIR" ]; then
        # Execute session file on behalf of file owner
        find $MINI_X_SESSION_DIR -type f | while read SESSIONFILE; do
                set +e
                USERNAME=`stat -c %U $SESSIONFILE`
                # Using su rather than sudo as latest 1.8.1 cause failure [YOCTO #1211]
#               su -l -c '$SESSIONFILE&' $USERNAME
                sudo -b -i -u $USERNAME $SESSIONFILE&
                set -e
        done
fi

xrandr -s 1600x900

export DISPLAY=:0
xset s off
xset -dpms
xset s noblank

sleep 10                   

su - kiosk -c '
  dbus-run-session -- env DISPLAY=:0 WEBKIT_DISABLE_DMABUF_RENDERER=1 \
    epiphany --incognito-mode \
             --profile="$HOME/.local/share/org.gnome.Epiphany.WebApp-kiosk" \
             localhost
' &

sleep 10

xdotool key "F11" &

exec matchbox-window-manager

EOF
chmod +x /usr/bin/x-session-manager
id kiosk 2>/dev/null || useradd -m -s /bin/sh -U kiosk
KUID=$(id -u kiosk)
mkdir -p /run/user/$KUID
chown kiosk:kiosk /run/user/$KUID
chmod 700 /run/user/$KUID
tee /etc/X11/Xsession.d/15-xhost-kiosk <<'EOF'
#!/bin/sh
# Allow the kiosk user to connect to the local X server
xhost +SI:localuser:kiosk >/dev/null 2>&1 || true
EOF
chmod +x /etc/X11/Xsession.d/15-xhost-kiosk

systemctl disable hciuart.service
systemctl disable bluealsa.service
systemctl disable bluetooth.service.
systemctl stop bluetooth bthelper@hci0.service 2>/dev/null || true
systemctl disable bluetooth bthelper@hci0.service 2>/dev/null || true
systemctl mask bluetooth bthelper@.service 2>/dev/null || true
opkg remove bluez5 bluez5-noinst-tools 2>/dev/null || true
cat >/etc/modprobe.d/blacklist-bluetooth.conf <<'EOF'
blacklist btbcm
blacklist hci_uart
blacklist btintel
blacklist btrtl
blacklist btqca
blacklist btusb
blacklist bluetooth
EOF

hostnamectl set-hostname "TDN-POD-ACK_v2.5"
chmod +x /usr/bin/procscan
mv -v /root/app/app/SHA ~/.SHA
npm --prefix /root/install install /root/app/tdn-ethv3-3.1.0.tgz
rm -r ~/.node-red/
mv /root/install/node_modules/tdn-ethv3/ /root/.node-red/
systemctl mask NetworkManager.service
systemctl mask networking.service
systemctl enable systemd-networkd.service
systemctl enable systemd-resolved.service
cat <<EOF | tee /etc/systemd/network/10-eth0.network
[Match]
Name=eth0

[Network]
DHCP=yes
Address=192.168.0.20/24
Gateway=192.168.0.1
EOF

cd .node-red/
npm update
vi settings.js
echo "e5c726c6079415926c7526b277bcf69b4ec1010c8c887322cca47ad2cb658c8b" > .SHA
su -c "cp /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.bak && sed -i 's|^\(\s*\)let digestCreds = this\.credentials;|\1var digestUser = msg.digestUser;\n\1var digestPass = msg.digestPass;\n\1let digestCreds = {"user":digestUser,"password":digestPass};|' /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js" root
cp -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/
su -c reboot root
```