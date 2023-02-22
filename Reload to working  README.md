sudo -s
sudo nano /etc/fstab 
cd Downloads/
ls
./NVIDIA-Linux-x86_64-525.85.05.run 
sudo ./NVIDIA-Linux-x86_64-525.85.05.run 
sudo reboot
ln --h

sudo nano /etc/fstab 
sudo reboot
ubuntu-drivers devices
sudo ubuntu-drivers autoinstall
sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev
ssh-keygen -t rsa -b 4096 -C "switchpoint.online@gmail.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_rsa
ls -al ~/.ssh
cat ~/.ssh
cat ~/.ssh/id_rsa.pub 
sudo apt install geany
sudo apt install curl
curl -s https://deb.nodesource.com/setup_16.x | sudo bash
sudo apt install nodejs -y
bash <(curl -sL https://raw.githubusercontent.com/node-red/linux-installers/master/deb/update-nodejs-and-nodered)
sudo systemctl mask nodered
sudo node-red
exit
sudo apt install anydesk
npm i npm-pack-all -g
sudo npm i npm-pack-all -g
npm audit
cd /usr/lib/node_modules/
ls
cd npm-pack-all/
ls
npm-audit
npm audit
sudo npm audit
npm-pack-all 
ls
cd ..
cd ~
cd Downloads/
ls .deb
ls *.deb
sudo apt install ./code_1.75.1-1675893397_amd64.deb 
sudo apt  install ./anydesk_6.2.1-1_amd64.deb 
sudo apt install snap
sudo apt install wget
sudo apt install gpg
cd ~
sudo apt -y install libusb-1.0-0-dev
cd Yocto/meta-extras/
git pull
git status
git add recipes-tdn-nr/tdn-nr/tdn-nr_1.0.1.bb 
git commit
git config --global user.email "switchpoint.online@gmail.com"
git config --global user.name "SwitchPoint-Online"
git commit
git push
git push origin langdale-v3
cat ~/.ssh/id_rsa.pub 
cd ~
exit
ssh-keygen -t rsa -b 4096 -C "switchpoint.online@gmail.com"
eval "$(ssh-agent -s)"
ssh -T git@github.com
ssh-add ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub 
ssh -T git@github.com
cd Yocto/meta-extras/
git push origin langdale-v3
cd ..
source poky/oe-init-build-env
bitbake core-image-base --runonly=fetch
sudo apt install pzstd
sudo apt install zstd
bitbake core-image-base --runonly=fetch
bitbake core-image-base
exit
