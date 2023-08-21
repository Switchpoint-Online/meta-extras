SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=Langdale"
SRC_URI[sha256sum] = "5bce76250f8d9b257ef202c79a27967157b3b28f02a2b8300f964379ddb9aa50"


SRCREV = "930713349bed200c5a4c52e7f949cce3fdeb1bad"

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/.node-red
    cp -R ${MY_FILES}/* ${D}/home/root/.node-red
}

FILES:${PN}= "/home/root/.node-red"
