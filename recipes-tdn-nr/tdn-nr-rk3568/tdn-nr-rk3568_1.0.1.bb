SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=dunfell"
SRC_URI[sha256sum] = "54efe9e8d8f666e4c6da46fd959f3b47b026ec212da89fffd54cd3658b39af1c"

SRCREV = "d172ad39bd69fbb9b5e2a240b78841f131f07e2c"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/app
    cp -R ${MY_FILES}/* ${D}/home/root/app
}

FILES:${PN}= "/home/root/app"
