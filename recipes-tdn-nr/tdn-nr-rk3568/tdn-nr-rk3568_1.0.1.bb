SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=dunfell"
SRC_URI[sha256sum] = "8562047a35dbc8778ec7614d83968ff48795d0be0515aa19ce9e71452240ce2b"

SRCREV = "b2e0c49ca40dc5dda3e509c5ceaca52051d402b6"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_install() {
    install -d ${D}/root
    mkdir ${D}/root/.node-red
    cp -R ${MY_FILES}/* ${D}/root/.node-red
}

FILES:${PN}= "/root/.node-red"
