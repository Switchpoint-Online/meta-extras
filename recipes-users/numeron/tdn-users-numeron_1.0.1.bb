SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"

inherit extrausers

NUMERONSRV_PASS = "transfer"

EXTRA_USERS_PARAM = "
        useradd -p 'openssl passwd ${NUMERONSRV_PASS}' numeronsrv; \
        groupadd ftp; \
        usermod -g ftp config; \
        "