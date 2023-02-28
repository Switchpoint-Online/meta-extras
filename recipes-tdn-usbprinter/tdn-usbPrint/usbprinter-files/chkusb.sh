#!/bin/bash
#
# Util to check USB subsystem for Linux kernel 3.12+ on TI Sitara devices
#
# Copyright (C) 2017 Texas Instruments Incorporated - http://www.ti.com/
#
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions
#  are met:
#
#    Redistributions of source code must retain the above copyright
#    notice, this list of conditions and the following disclaimer.
#
#    Redistributions in binary form must reproduce the above copyright
#    notice, this list of conditions and the following disclaimer in the
#    documentation and/or other materials provided with the
#    distribution.
#
#    Neither the name of Texas Instruments Incorporated nor the names of
#    its contributors may be used to endorse or promote products derived
#    from this software without specific prior written permission.
#
#  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
#  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
#  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
#  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
#  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
#  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
#  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
#  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
#  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

VERSION=0.2.4


### functions ###

# $1 command to be checked
check_command() {
    local _cmd=$1

    which $_cmd > /dev/null || {
        echo "Error: $_cmd command not found"
        exit 1
    }
}

# check if the kernel is supported
# this tool only runs on v3.12+ kernel
# return 0 - if kernel version >= 3.12
#        1 - if kernel version < 3.12
has_supported_kernel() {
	local _ver
	local _t

    check_command uname
    uname -a
	_ver=`uname -r`
    _t=${_ver%%.*}
    # 2.x.x, unsupported
    [ $_t -ge 3 ] || return 1
    # 4.x.x, supported
    [ $_t -lt 4 ] || return 0

    _ver=${_ver#*.}
    _t=${_ver%%.*}
    # < 3.12.x, unsupported
    [ $_t -ge 12 ] || return 1
    return 0
}

# check if the platform is supported
#    $PLATFORM - global variable
# return 0 - if platform is supported
#        1 - if platform is not supported
check_platform () {
	local _hw

    check_command grep
    [ "$PLATFORM" != "" ] || {
	    _hw=`grep '^Hardware' /proc/cpuinfo`
	    DBG_PRINT $_hw

        if [ "${_hw#*AM33XX}" != "$_hw" ]; then
            PLATFORM="am335x"
        elif [ "${_hw#*AM43}" != "$_hw" ]; then
            PLATFORM="am437x"
        elif [ "${_hw#*DRA7}" != "$_hw" ]; then
            PLATFORM="am57x"
        elif [ "${_hw#*Keystone}" != "$_hw" ]; then
            PLATFORM="keystone"
        else
            PLATFORM="unknown"
        fi
    }

    DBG_PRINT $PLATFORM
    case $PLATFORM in
        "am335x")
            USB0='usb@47400000/usb@47401000'
            USB1='usb@47400000/usb@47401800'
            return 0;;
        "am437x")
            USB0='omap_dwc3@48380000/usb@48390000'
            USB1='omap_dwc3@483c0000/usb@483d0000'
            return 0;;
        "am57x")
            USB0='omap_dwc3_1@48880000/usb@48890000'
            USB1='omap_dwc3_2@488c0000/usb@488d0000'
            return 0;;
        "keystone")
            USB0='omap_dwc3_1@48880000/usb@48890000'
            USB1='omap_dwc3_2@488c0000/usb@488d0000'
            return 0;;
        *)
            echo "Unsupported $PLATFORM"
            return 1;;
    esac
}

# check a kernel CONFIG option
# params $1 - the config option
#        $2 = '-q', quiet output
# return 0 - undefined
#        1 - defined as 'm', kernel module
#        2 - defined as 'y', kernel builtin
check_kernel_config() {
    local _cfg

    [ -n "$1" ] || return 0
    check_command zcat
    _cfg=`zcat /proc/config.gz | grep "^$1\>"`

    case ${_cfg#*=} in
        "y") return 2;;
        "m") return 1;;
          *) [ "$2" = "-q" ] ||
              echo "Error: $1 is undefined in kernel config"
          return 0;;
    esac
}

# check a kernel module
# $1 - module name, relative path from drivers/, with .ko surfix
# return 0 - found
#        1 - error
check_module() {
    local _modname
    local _moddep

    [ -n "$1" ] || return 1

    _modname="/lib/modules/`uname -r`/kernel/drivers/${1}.ko"
    _moddep="/lib/modules/`uname -r`/modules.dep"

    DBG_PRINT 1
    [ -f $_modname ] || {
        echo "Error: $_modname not found."
        echo "       Please ensure 'make module_install' is done properly."
        return 1
    }

    DBG_PRINT 2
    [ -f $_moddep ] || $moddep_checked || {
        echo "Error: $_moddep not found."
        echo "       Please ensure 'make module_install' is done properly."
        moddep_checked=true
    }

    DBG_PRINT 3
    check_command lsmod
    check_command basename
    check_command tr

    lsmod | grep `basename $1 | tr '-' '_'` > /dev/null || {
        DBG_PRINT ">>>> ${1}.ko:"
        if grep "${1}.ko:" $_moddep > /dev/null; then
            DBG_PRINT 5
            echo "Error: $_moddep seems to be valid,"
            echo "       but `basename $1`.ko is not loaded."
            echo "       Please provide /proc/config.gz and /lib/module/`uname -r`/*"
            echo "       for further investigation."
        else
            DBG_PRINT 6
            echo "Error: `basename $1`: $_moddep is invalid."
            echo "       Please run command 'depmod' on the target to re-generate it,"
            echo "       then reboot the target. If the issue still exists, please"
            echo "       ensure 'make module_install' is done properly."
        fi

        DBG_PRINT 7
        return 1
    }
    DBG_PRINT 8
    return 0
}

# check kernel config, and modules (if CONFIG_*=M) for musb
check_musb_drivers() {
    check_kernel_config CONFIG_USB_MUSB_HDRC
    [ $? != 1 ] || check_module 'usb/musb/musb_hdrc'

    check_kernel_config CONFIG_USB_MUSB_DUAL_ROLE -q
    [ $? != 0 ] || echo "Warning: CONFIG_USB_MUSB_DUAL_ROLE undefined."

    check_kernel_config CONFIG_USB_MUSB_DSPS
    [ $? != 1 ] || {
        check_module 'usb/musb/musb_dsps'
        check_module 'usb/musb/musb_am335x'
    }

    check_kernel_config CONFIG_AM335X_PHY_USB
    [ $? != 1 ] || {
        check_module 'usb/phy/phy-am335x'
        check_module 'usb/phy/phy-am335x-control'
    }

    check_kernel_config CONFIG_MUSB_PIO_ONLY -q
    [ $? != 0 ] || {
       if check_kernel_config CONFIG_TI_CPPI41 -q; then
           echo "Error: MUSB CPPI DMA mode is enabled, but CPPI moudle is not enabled in DMA Engine."
           echo "       Please enable CONFIG_TI_CPPI41 under DMA Engine Support in kernel config."
       fi
    }
}

# check kernel config, and modules (if CONFIG_*=M) for dwc3
check_dwc3_drivers() {
    check_kernel_config CONFIG_USB_DWC3
    [ $? != 1 ] || check_module 'usb/dwc3/dwc3'

    check_kernel_config CONFIG_USB_DWC3_DUAL_ROLE -q
    [ $? != 0 ] || echo "Warning: CONFIG_USB_DWC3_DUAL_ROLE undefined."

    check_kernel_config CONFIG_USB_OTG -q
    [ $? != 0 ] || echo "Warning: CONFIG_USB_OTG undefined, which is required for DRD mode."

    check_kernel_config CONFIG_USB_DWC3_OMAP
    [ $? != 1 ] || check_module 'usb/dwc3/dwc3-omap'

    check_kernel_config CONFIG_USB_XHCI_HCD
    [ $? != 1 ] || {
        check_module 'usb/host/xhci-plat-hcd'
        check_module 'usb/host/xhci-hcd'
    }

    check_kernel_config CONFIG_OMAP_CONTROL_PHY
    [ $? != 1 ] || check_module 'phy/phy-omap-control'

    if [ $PLATFORM = am437x ]; then
        check_kernel_config CONFIG_OMAP_USB2
        [ $? != 1 ] || check_module 'phy/phy-omap-usb2'
    else
        check_kernel_config CONFIG_TI_PIPE3
        [ $? != 1 ] || check_module 'phy/phy-ti-pipe3'
    fi
}

### debug ###

g_log_file=/tmp/chkusb.log

DBG_ENABLE() { g_dbg_enabled=true; }
DBG_DISABLE() { g_dbg_enabled=false; }
DBG_LOG_RESET() { ! $g_dbg_enabled || echo > $g_log_file; }
DBG_PRINT() { ! $g_dbg_enabled || echo "$(date +%H:%M:%S) [$(basename $0)]: $*"; }
DBG_LOG() { DBG_PRINT $* >> $g_log_file; }
DBG_LOG_MARK() { DBG_PRINT "----------------" >> $g_log_file; }


### main ####

moddep_checked=false

echo "chkusb.sh Version $VERSION"

[ "$V" = "1" ] && DBG_ENABLE && DBG_LOG_RESET || DBG_DISABLE

has_supported_kernel ||
    { echo "Unsupported kernel version: `uname -r`"; exit 1; }
check_platform || exit 2
DBG_PRINT device: $PLATFORM

check_command lsusb
if lsusb > /dev/null 2>&1; then
    echo "USB is initialized"
else
    echo "USB initialization failed"

    # failed, continue checking
    [ -f /proc/config.gz ] ||
        { echo "Error: /proc/config.gz not found"; exit 4; }

    case $PLATFORM in
        am335x) check_musb_drivers;;
        am437x | am57x | keystone) check_dwc3_drivers;;
        *)
            echo "Error: unsupported platform $PLATFORM"
            exit 5;;
    esac
fi

# check dr_mode & gadget drivers

[ -d /proc/device-tree ] || {
    echo "Warning: /proc/device-tree/ not found"
    if [ -d "/lib/modules/`uname -r`/" ]; then
        echo "The list of USB gadget drivers installed:"
        ls -1Rp "/lib/modules/`uname -r`/kernel/drivers/usb/gadget/"
    fi
    exit 0
}

check_command basename
for _usb in "${USB0}" "${USB1}"; do
    _usb_dir="/proc/device-tree/ocp/${_usb}"
    _status=`cat $_usb_dir/status 2> /dev/null`
    _dr_mode=`cat $_usb_dir/dr_mode`
    echo `basename $_usb`: $_dr_mode, $_status

    [ "$_status" = "disabled" -o "$_dr_mode" = "host" ] || gadget_mode=true
done

DBG_PRINT $gadget_mode
$gadget_mode || exit 0

echo

check_kernel_config CONFIG_USB_LIBCOMPOSITE
case $? in
    0) echo "Error: no any gadget driver enabled"
       exit 6;;
    1) is_gadget_builtin=false;;
    2) echo "The gadget driver is built-in"
       is_gadget_builtin=true;;
esac

check_kernel_config CONFIG_USB_ZERO -q ||
    echo "Gadget Kernel Config: g_zero is enabled"
check_kernel_config CONFIG_USB_AUDIO -q ||
    echo "Gadget Kernel Config: g_audio is enabled"
check_kernel_config CONFIG_USB_ETH -q ||
    echo "Gadget Kernel Config: g_ether is enabled"
check_kernel_config CONFIG_USB_G_NCM -q ||
    echo "Gadget Kernel Config: g_ncm is enabled"
check_kernel_config CONFIG_USB_MASS_STORAGE -q ||
    echo "Gadget Kernel Config: g_mass_storage is enabled"
check_kernel_config CONFIG_USB_G_SERIAL -q ||
    echo "Gadget Kernel Config: g_serial is enabled"
check_kernel_config CONFIG_USB_G_PRINTER -q ||
    echo "Gadget Kernel Config: g_printer is enabled"

g_driver=`grep '^DRIVER=' /sys/class/udc/*/uevent 2>/dev/null`
echo "gadget driver loaded: ${g_driver:-(none)}"

echo

if ! $is_gadget_builtin; then
    if [ -d "/lib/modules/`uname -r`/" ]; then
        echo "The list of USB gadget drivers installed:"
        ls -1Rp "/lib/modules/`uname -r`/kernel/drivers/usb/gadget/"
    else
        echo "Error: /lib/modules/`uname -r`/ not found"
        echo "       Please ensure 'make modules_install' is done properly."
        exit 7
    fi
fi

# vim: ft=sh:ts=4:sw=4:et
