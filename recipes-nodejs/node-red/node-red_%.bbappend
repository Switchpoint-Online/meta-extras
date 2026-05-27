do_install:append() {
    NR_HTTP="${D}/usr/lib/node_modules/node-red/node_modules/@node-red/nodes/core/network/21-httprequest.js"
    if [ -f "${NR_HTTP}" ]; then
        cp ${NR_HTTP} ${NR_HTTP}.bak
        sed -i 's|^\(\s*\)let digestCreds = this\.credentials;|\1var digestUser = msg.digestUser;\n\1var digestPass = msg.digestPass;\n\1let digestCreds = {"user":digestUser,"password":digestPass};|' \
            ${NR_HTTP}
    fi
}
