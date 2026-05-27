SUMMARY = "TDN Node-RED base packages + procscan binary — common to all products"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://procscan.c \
           file://package.json"

S = "${WORKDIR}"

DEPENDS = "nodejs-native"
RDEPENDS:${PN} = "nodejs node-red"

# npm must download node-red-dashboard and other dependencies from the registry.
do_compile[network] = "1"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -o ${WORKDIR}/procscan ${WORKDIR}/procscan.c

    export PUPPETEER_SKIP_DOWNLOAD=true
    export npm_config_cache="${WORKDIR}/npm-cache"

    # `npm --prefix X install /dir` (npm v10) only places the local package itself
    # into X/node_modules — it does NOT install that package's own deps at prefix
    # level.  Instead: copy package.json into the prefix and run `npm install`
    # (no-arg) so npm resolves and installs all deps directly in X/node_modules/.
    mkdir -p ${WORKDIR}/install
    cp ${WORKDIR}/package.json ${WORKDIR}/install/package.json
    npm --prefix ${WORKDIR}/install install
}

# @serialport/bindings-cpp ships prebuilds/ for every platform.
# Yocto QA fails on foreign ELF/PE binaries. Only linux-arm64 is needed.
# libc is always present — suppress file-rdeps for the kept native addon.
INSANE_SKIP:${PN} = "file-rdeps"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/procscan ${D}${bindir}/procscan

    install -d ${D}/usr/share/tdn-nr-base
    cp -r ${WORKDIR}/install/node_modules ${D}/usr/share/tdn-nr-base/

    # Remove prebuilt native binaries for non-AArch64 platforms.
    find ${D}/usr/share/tdn-nr-base -type d -name "prebuilds" | while IFS= read -r dir; do
        find "$dir" -mindepth 1 -maxdepth 1 -type d ! -name "linux-arm64" -exec rm -rf {} +
    done

    # tslib ships demo HTML files (tslib.html, tslib.es6.html) that are NOT
    # Node-RED nodes. Node-RED's nodesDir scanner loads every top-level package
    # regardless of keywords, serves these HTML files to the browser, which then
    # requests tslib.js / tslib.es6.js from the admin root (they don't exist
    # there) → MIME-blocked 404 → editor stalls at "Loading Nodes X/Y".
    # These files have zero runtime value; remove them from ALL tslib installs.
    find ${D}/usr/share/tdn-nr-base -path "*/tslib/tslib*.html" -delete
}

FILES:${PN} = " \
    ${bindir}/procscan \
    /usr/share/tdn-nr-base \
"
