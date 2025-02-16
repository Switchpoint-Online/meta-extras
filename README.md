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

### TDN-EWS 
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
#### Run as root
su root
```
hostnamectl set-hostname "TDN-GSIv3"
chmod +x /usr/bin/procscan
mv -v /root/app/app/SHA ~/.SHA
npm --prefix /root/install install /root/app/tdn-ftp_v2-2.0.2.tgz
rm -r ~/.node-red/
mv /root/install/node_modules/tdn-ftp_v2/ /root/.node-red/
mv -v /root/app/app/lib/ui-media/lib/ui/* /root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
timedatectl set-ntp false
```