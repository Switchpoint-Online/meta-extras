SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"
MY_FILES = "${THISDIR}/nr-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=dunfell"

SRC_URI[sha256sum] = "54efe9e8d8f666e4c6da46fd959f3b47b026ec212da89fffd54cd3658b39af1c"

SRCREV = "bfafda71de5c4b91bcf6ee7c6578dd50fec9edca"

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/app
    cp -R ${MY_FILES}/* ${D}/home/root/app
}

FILES:${PN}= "/home/root/app"
