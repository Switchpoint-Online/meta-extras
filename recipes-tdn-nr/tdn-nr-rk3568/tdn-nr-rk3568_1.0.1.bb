SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=dunfell"
SRC_URI[sha256sum] = "54efe9e8d8f666e4c6da46fd959f3b47b026ec212da89fffd54cd3658b39af1c"

SRCREV = "891adb79d0037c64d3a489822c9f0c72471ef56a"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_install() {
    install -d ${D}/root
    mkdir ${D}/root/.node-red
    cp -R ${MY_FILES}/* ${D}/root/.node-red
}

FILES:${PN}= "/root/.node-red"
