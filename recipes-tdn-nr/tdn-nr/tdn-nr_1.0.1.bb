SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale"
# SRC_URI[sha256sum] = "8562047a35dbc8778ec7614d83968ff48795d0be0515aa19ce9e71452240ce2b"

SRCREV = "2fc7995c4215f58a433c1d702f716b515714cff1"
# BPV = "0.1.0"
# PV = "${BPV}+gitr${SRCPV}" 

do_install() {
    install -d ${D}/root
    mkdir ${D}/root/.node-red
    cp -R ${MY_FILES}/* ${D}/root/.node-red
}

FILES:${PN}= "/root/.node-red"
