# Let BitBake find files stored next to this bbappend under linux-raspberrypi/
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Keep your existing config fragment
SRC_URI:append = " file://userns.cfg"

# ---- CM0 device tree integration ----
# 1) Your real CM0 DTS (from the working Raspberry Pi Imager image)
# 2) An arm64 wrapper DTS that includes the real CM0 DTS from the arm tree
SRC_URI:append = " file://bcm2710-rpi-cm0.dts file://bcm2710-rpi-cm0-arm64-wrapper.dts"

do_configure:append() {
    # Install the REAL CM0 DTS into the 32-bit Broadcom DTS directory.
    # The DTS you have uses: #include "bcm2710.dtsi"
    # That include model matches the Raspberry Pi kernel's arch/arm DTS layout.
    if [ -d ${S}/arch/arm/boot/dts/broadcom ]; then
        install -m 0644 ${WORKDIR}/bcm2710-rpi-cm0.dts \
            ${S}/arch/arm/boot/dts/broadcom/
    else
        bbfatal "Cannot find ${S}/arch/arm/boot/dts/broadcom to install bcm2710-rpi-cm0.dts"
    fi

    # Install the ARM64 wrapper DTS into the 64-bit Broadcom DTS directory,
    # but name it bcm2710-rpi-cm0.dts so the arm64 dtbs build produces bcm2710-rpi-cm0.dtb.
    if [ -d ${S}/arch/arm64/boot/dts/broadcom ]; then
        install -m 0644 ${WORKDIR}/bcm2710-rpi-cm0-arm64-wrapper.dts \
            ${S}/arch/arm64/boot/dts/broadcom/bcm2710-rpi-cm0.dts
    else
        bbfatal "Cannot find ${S}/arch/arm64/boot/dts/broadcom to install arm64 wrapper DTS"
    fi
}
