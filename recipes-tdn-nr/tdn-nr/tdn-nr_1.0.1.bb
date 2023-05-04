SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale"
SRC_URI[sha256sum] = "9afa57eb7eb5dc89f989097eeff9ad81c703c7a74ec08f08ed3a5d302e78106d"

SRCREV = "${AUTOREV}"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
}

FILES:${PN}= "/home/root/.node-red"
