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
#### 