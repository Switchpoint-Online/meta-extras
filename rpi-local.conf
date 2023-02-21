MACHINE ??= "raspberrypi0-2w"
LICENSE_FLAGS_ACCEPTED = "synaptics-killswitch"
DISTRO ?= "poky"
MACHINE_FEATURES = "wifi"
MACHINE_FEATURES += "pitft pitft22"
ENABLE_UART = "1"
ENABLE_DWC2_PERIPHERAL = "1"
# ENABLE_DWC2_HOST = "1"
DISABLE_SPLASH = "1"
DISABLE_RPI_BOOT_LOGO = "1"
INHERIT += "extrausers"
ROOT_PASSWORD = "\$5\$zbhcKTd9d5tbZAU\$oWA7RvXvYM/ND55qM7r/vmvWwQFAXKWK9XgTOGto710"
EXTRA_USERS_PARAMS = "usermod -p '${ROOT_PASSWORD}' root"
IMAGE_INSTALL:append = " nodejs nodejs-npm node-red wpa-supplicant networkmanager"
IMAGE_INSTALL:append = " tdn-nr"
# KERNEL_MODULE_AUTOLOAD:rpi += " g_printer"
DISTRO_FEATURES:append = " usbgadget systemd"
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
