SUMMARY = "TDN USB Printer Extras"
DESCRIPTION = "printer"
HOMEPAGE = ""
LICENSE = "CLOSED"

MY_FILES = "${THISDIR}/usbprinter-files"

SRC_URI += "https://github.com/Switchpoint-Online/meta-extras.git;protocol=ssh;branch=kirkstone"
SRC_URI[sha256sum] = "d2e68169cb0f434415f2cf41ffadd8c06bcd0a5228d698b9046821766118912e"

do_install() {
    install -d ${D}/home/root
    mkdir ${D}/home/root/usbprinter
    cp -R ${MY_FILES}/* ${D}/home/root/usbprinter
}

FILES:${PN}= "/home/root/usbprinter"