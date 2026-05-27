SUMMARY = "TDN systemd-networkd config — br0 bridge over eth* for static LAN anchor"
DESCRIPTION = "\
    Creates a Linux bridge (br0) and enslaves all eth* interfaces to it. \
    br0 gets DHCP (IPv4 only). WiFi is not bridged. \
    Files are placed in /etc/systemd/network/ so they take priority over \
    the upstream /usr/lib/systemd/network/80-wired.network which would \
    otherwise assign DHCP directly to eth0. \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://10-br0.netdev \
    file://10-br0.link \
    file://20-br0.network \
    file://30-eth.network \
"

S = "${WORKDIR}"

inherit allarch

RDEPENDS:${PN} = "systemd"

do_install() {
    install -d ${D}/etc/systemd/network

    # 10-br0.netdev  — create the bridge device
    install -m 0644 ${WORKDIR}/10-br0.netdev  ${D}/etc/systemd/network/

    # 10-br0.link — pin MAC policy so br0 inherits eth0's OTP-burned MAC
    # instead of getting a new random address each boot
    install -m 0644 ${WORKDIR}/10-br0.link    ${D}/etc/systemd/network/

    # 20-br0.network — DHCP on br0 (IPv4 only; no IPv6 RA)
    install -m 0644 ${WORKDIR}/20-br0.network ${D}/etc/systemd/network/

    # 30-eth.network — enslave eth* to br0
    # Priority 30 < 80, so this shadows the upstream 80-wired.network
    # that would otherwise hand DHCP directly to eth0.
    install -m 0644 ${WORKDIR}/30-eth.network ${D}/etc/systemd/network/
}

FILES:${PN} = " \
    /etc/systemd/network/10-br0.netdev \
    /etc/systemd/network/10-br0.link \
    /etc/systemd/network/20-br0.network \
    /etc/systemd/network/30-eth.network \
"
