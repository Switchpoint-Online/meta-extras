#
# Machine Selection
#
# You need to select a specific machine to target the build with.
MACHINE ?= "radxa-cm3-io-rk3566"
#MACHINE ?= "radxa-zero-s905y2"
#MACHINE ?= "rockpi-4a-rk3399"
#MACHINE ?= "rockpi-4b-rk3399"
#MACHINE ?= "rockpi-4c-rk3399"
#MACHINE ?= "rockpi-e-rk3328"
#MACHINE ?= "rockpi-n10-rk3399pro"
#MACHINE ?= "rockpi-px30"
#MACHINE ?= "rockpi-s-rk3308"
# MACHINE ?= "rock-3a-rk3568"

DISTRO ?= "poky"
ENABLE_UART = "1"
hostname_pn-base-files = "TDN-GSIv3"
PACKAGE_CLASSES = "package_deb"
EXTRA_IMAGE_FEATURES += " package-management"
# EXTRA_IMAGE_FEATURES ?= "debug-tweaks"
DISTRO_FEATURES:append = " systemd networkd pam xz"
# DISTRO_FEATURES_append = " x11"
DISTRO_FEATURES:remove = " sysvinit wayland"

VIRTUAL-RUNTIME_init_manager = "systemd"
PACKAGECONFIG_append_pn-systemd = " resolved networkd"
USER_CLASSES ?= "buildstats image-mklibs image-prelink"
INHERIT += "extrausers"
ROOT_PASSWORD = "\$5\$zbhcKTd9d5tbZAU\$oWA7RvXvYM/ND55qM7r/vmvWwQFAXKWK9XgTOGto710"
EXTRA_USERS_PARAMS = "usermod -p '${ROOT_PASSWORD}' root"
PATCHRESOLVE = "noop"
BB_DISKMON_DIRS ??= "\
    STOPTASKS,${TMPDIR},1G,100K \
    STOPTASKS,${DL_DIR},1G,100K \
    STOPTASKS,${SSTATE_DIR},1G,100K \
    STOPTASKS,/tmp,100M,100K \
    ABORT,${TMPDIR},100M,1K \
    ABORT,${DL_DIR},100M,1K \
    ABORT,${SSTATE_DIR},100M,1K \
    ABORT,/tmp,10M,1K"

# Uncomment the following line to add the extra packages
#IMAGE_INSTALL_append = " create-ap"
# IMAGE_INSTALL:append = " curl rdate nodejs nodejs-npm node-red"
IMAGE_INSTALL:append = " tdn-nr-rk3568"
CONF_VERSION = "1"