sudo systemctl enable nodered.service
sudo npm config set unsafe-perm true
sudo systemctl unmask nodered.service
sudo systemctl enable nodered.service
d=/lib/systemd/system/nodered.service && sudo sed "s/User=pi/User=root/;s/Group=pi/Group=root/" $d > tmp && sudo mv -f tmp $d
systemctl start node-red
systemctl start nodered
systemctl daemon reload
systemctl daemon-reload
systemctl start nodered
exit
sudo systemctl enable nodered.service
sudo npm config set unsafe-perm true
d=/lib/systemd/system/nodered.service && sudo sed "s/User=pi/User=root/;s/Group=pi/Group=root/" $d > tmp && sudo mv -f tmp $d
d=/root/.node-red/settings.js && sudo sed "/.*userDir:*./c\userDir: '\/home\/pi\/.node-red\/'," $d > tmp && sudo mv -f tmp $d
d=/boot/config.txt && sudo sed "/.*dtparam=audio=on*./c\dtparam=audio=off" $d > tmp && sudo cp -f tmp $d