SUMMARY = "Create kiosk runtime user"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit extrausers allarch
PACKAGES = "${PN}"
ALLOW_EMPTY:${PN} = "1"
EXTRA_USERS_PARAMS = "\
  useradd -m -d /home/kiosk -s /bin/sh kiosk; \
  usermod -p '\$6\$rounds=4096\$mysalt\$myhash...' kiosk; \
"
