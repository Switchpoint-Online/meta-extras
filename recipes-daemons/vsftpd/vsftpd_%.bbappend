FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Replace the upstream vsftpd.conf with TDN config:
# - local users only (no anonymous)
# - whitelist via /etc/vsftpd.userlist (numeronsrv only)
# - chroot to /tmp/ftp (RAM, cleaned on reboot)
# - check_shell=NO so numeronsrv (/usr/sbin/nologin shell) can still FTP
SRC_URI:append = " file://vsftpd.userlist"

do_install:append() {
    install -m 0644 ${WORKDIR}/vsftpd.userlist ${D}${sysconfdir}/vsftpd.userlist
}

FILES:${PN} += "${sysconfdir}/vsftpd.userlist"
