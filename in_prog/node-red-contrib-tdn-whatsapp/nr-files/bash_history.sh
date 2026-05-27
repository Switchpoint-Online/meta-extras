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
