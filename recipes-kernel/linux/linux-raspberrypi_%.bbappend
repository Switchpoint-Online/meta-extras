FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://userns.cfg"

# ---- CM0 device tree integration ----
SRC_URI:append = " file://bcm2710-rpi-cm0.dts"

do_configure:append() {
    # 64-bit kernels keep Broadcom Raspberry Pi DTS files here:
    if [ -d ${S}/arch/arm64/boot/dts/broadcom ]; then
        install -m 0644 ${WORKDIR}/bcm2710-rpi-cm0.dts ${S}/arch/arm64/boot/dts/broadcom/
    # 32-bit kernels keep Broadcom Raspberry Pi DTS files here:
    elif [ -d ${S}/arch/arm/boot/dts/broadcom ]; then
        install -m 0644 ${WORKDIR}/bcm2710-rpi-cm0.dts ${S}/arch/arm/boot/dts/broadcom/
    else
        bbfatal "Cannot find kernel DTS directory (arch/arm64/boot/dts/broadcom or arch/arm/boot/dts/broadcom)."
    fi
}
