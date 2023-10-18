SUMMARY = "TDN Extras"
DESCRIPTION = ""
HOMEPAGE = ""
LICENSE = "CLOSED"

inherit extrausers

CONFIG_PASS = "r8"

EXTRA_USERS_PARAM = "
        useradd -p 'openssl passwd ${CONFIG_PASS}' config; \
        groupadd ipdev; \
        usermod -g ipdev config; \
        "