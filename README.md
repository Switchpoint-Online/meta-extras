# meta-extras
## Raspberry Pi Langdale BSP
cd ~/Yocto/Langdale/
git clone -b langdale git://git.yoctoproject.org/poky.git
git clone -b langdale git://git.yoctoproject.org/meta-raspberrypi.git
git clone -b langdale git://git.openembedded.org/meta-openembedded
git clone -b langdale https://github.com/intel-iot-devkit/meta-iot-cloud.git 
git clone git@github.com:Switchpoint-Online/meta-extras.git -b langdale-v1
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
cd tmp/deploy/images/raspberrypi0-2w/
bzip2 -d -f core-image-base-raspberrypi0-2w.wic.bz2
sudo dd bs=4M if=core-image-base-raspberrypi0-2w.wic of=/dev/sde status=progress conv=fsync
cd ~/Yocto/build/
exit

## Beaglebone Black Dunfell BSP
### Work in Progress
#### TODO 
Added Nelson Robert Kernal and get Modprobe functional 

cd Yocto/Dunfell/
git clone -b dunfell git://git.openembedded.org/meta-openembedded
git clone -b dunfell git://git.yoctoproject.org/poky.git
git clone -b dunfell https://github.com/meta-qt5/meta-qt5.git
git clone -b dunfell git://git.yoctoproject.org/meta-security.git
git clone -b dunfell https://github.com/jumpnow/meta-jumpnow.git
git clone -b dunfell https://github.com/jumpnow/meta-bbb.git
git clone -b dunfell https://github.com/intel-iot-devkit/meta-iot-cloud.git
git clone git@github.com:Switchpoint-Online/meta-extras.git
source poky/oe-init-build-env
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-multimedia
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-openembedded/meta-perl
bitbake-layers add-layer ../meta-bbb/
bitbake-layers add-layer ../meta-jumpnow/
bitbake-layers add-layer ../meta-qt5/
bitbake-layers add-layer ../meta-security/
bitbake-layers add-layer ../meta-extras/
bitbake-layers add-layer ../meta-iot-cloud/
bitbake console-image -n
bitbake console-image --runonly=fetch

./create_sdcard_image.sh 2 OETMP=/home/dev-env/Yocto/Dunfell/build/tmp/ /dev/sde
cp balenaEtcher-1.14.3-x64.AppImage /bin/balenaEtcher
sudo cp balenaEtcher-1.14.3-x64.AppImage /bin/balenaEtcher
balenaEtcher 


## RADXA CM3 IO Board - incl waveshare POE
### Working to test
mkdir Yocto/
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
bitbake -k radxa-console-image

#### Install rkdeveloptool
git clone https://github.com/rockchip-linux/rkdeveloptool.git
add rkdeveloptool to /bin PATH

#### load usb drivers to rk3568 and flash img
sudo rkdeveloptool db ~/Yocto/Rockpi/rk356x_spl_loader_ddr1056_v1.10.111.bin
sudo rkdeveloptool wl 0x0000 ~/Yocto/TDN-GSI-Radxa-cm3-Dunfell/build/tmp/deploy/images/radxa-cm3-io-rk3566/radxa-console-image-radxa-cm3-io-rk3566-gpt.img 
sudo rkdeveloptool rd

### setup security and install
rock 
su root
passwd root
cd ~/app
tar -xvf node-v16.20.1-linux-arm64.tar.xz
sudo cp -r ode-v16.20.1-linux-arm64/{bin,include,lib,share} /usr/
export PATH=/usr/node-v16.20.1-linux-arm64/bin:$PATH
sudo npm install -g --unsafe-perm node-red
npm --prefix /home/root/install install /home/root/app/nr.tgz
mv /home/root/install/node_modules/node-red-project/ /home/root/.node-red/
mv -v /home/root/app/app/lib/ui-media/lib/ui/* /home/root/.node-red/node_modules/node-red-dashboard/dist/
mv -v /home/root/app/app/21-httprequest.js /usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js
