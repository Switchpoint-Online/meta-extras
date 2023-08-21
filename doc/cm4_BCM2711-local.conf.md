MACHINE ??= "raspberrypi4-64"
LICENSE_FLAGS_ACCEPTED = "synaptics-killswitch"
DISTRO ?= "poky"
# MACHINE_FEATURES += "pitft pitft22"
ENABLE_UART = "1"
DISABLE_SPLASH = "1"
DISABLE_RPI_BOOT_LOGO = "1"
hostname_pn-base-files = "TDN-GSIv3"
INHERIT += "extrausers"
ROOT_PASSWORD = "\$5\$zbhcKTd9d5tbZAU\$oWA7RvXvYM/ND55qM7r/vmvWwQFAXKWK9XgTOGto710"
EXTRA_USERS_PARAMS = "usermod -p '${ROOT_PASSWORD}' root"
IMAGE_INSTALL:append = " rdate nodejs nodejs-npm node-red"
IMAGE_INSTALL:append = " tdn-nr"
DISTRO_FEATURES:append = " systemd networkd"
DISTRO_FEATURES:remove = " sysvinit"
VIRTUAL-RUNTIME_init_manager = "systemd"
USER_CLASSES ?= "buildstats"
PATCHRESOLVE = "noop"
BB_DISKMON_DIRS ??= "\
    STOPTASKS,${TMPDIR},1G,100K \
    STOPTASKS,${DL_DIR},1G,100K \
    STOPTASKS,${SSTATE_DIR},1G,100K \
    STOPTASKS,/tmp,100M,100K \
    HALT,${TMPDIR},100M,1K \
    HALT,${DL_DIR},100M,1K \
    HALT,${SSTATE_DIR},100M,1K \
    HALT,/tmp,10M,1K"
PACKAGECONFIG:append:pn-qemu-system-native = " sdl"
CONF_VERSION = "2"
