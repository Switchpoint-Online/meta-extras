SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale"
SRC_URI[sha256sum] = "e83a622367c366873a71fef9f0b88d15db2f11511d2055b2c945fd23a0e9dbeb"

SRCREV = "${AUTOREV}"

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
}

FILES:${PN}= "/home/root/.node-red"
