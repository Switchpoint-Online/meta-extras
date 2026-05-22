SUMMARY = "Create kiosk runtime user"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit extrausers allarch
PACKAGES = "${PN}"
ALLOW_EMPTY:${PN} = "1"
EXTRA_USERS_PARAMS = "\
  useradd -m -d /home/kiosk -s /bin/sh kiosk; \
  usermod -L kiosk; \
  usermod -a -G video,input kiosk; \
"

pkg_postinst:${PN} () {
    if [ -z "$D" ]; then
        mkdir -p /home/kiosk/.local/share
        chown -R kiosk:kiosk /home/kiosk
        chmod 700 /home/kiosk
        getent group render >/dev/null 2>&1 && usermod -a -G render kiosk || true
    fi
}