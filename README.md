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
useradd -p $(echo transfer | openssl passwd -1 -stdin) numeronsrv
chmod +x /usr/bin/procscan
cp -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-ftp_v2-2.0.2.tgz
cp /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
timedatectl set-ntp false
```
### setup security and install DiSU NODE
#### Run as root
su root
```
useradd -p $(echo r8 | openssl passwd -1 -stdin) config
chmod +x /usr/bin/procscan
mv -v /home/root/app/app/SHA ~/.SHA
npm --prefix /home/root/install install /home/root/app/tdn-disu_v2-2.0.2.tgz
mv /home/root/install/node_modules/tdn-ftp_v2/ /home/root/.node-red/
cp 21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
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
cp -rv /home/root/install/node_modules/tdn-bcd_v2/ /home/root/.node-red/
cp -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
cp -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
cp -v node-red.service /lib/systemd/system/node-red.service
cp -v /home/root/app/app/SHA ~/.SHA
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
npm install -g --unsafe-perm node-red
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

# DiSU Replacement Software
```

```