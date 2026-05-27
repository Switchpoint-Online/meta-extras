/**
 * Node-RED Settings — TDN-EWS (Yocto image)
 *
 * Based on the standard settings template (generated 2025-02-18).
 * TDN additions are marked with  ← TDN
 *
 * Do NOT edit /root/.node-red/settings.js directly on the device — changes
 * are overwritten on every reflash.  Edit this source file in the recipe:
 *   meta-extras/recipes-core/node-red-settings/files/settings.js
 */

module.exports = {

/*******************************************************************************
 * Flow File and User Directory Settings
 ******************************************************************************/

    flowFile: "flows.json",

    credentialSecret: "P@ssW0rd123",

    flowFilePretty: true,

    /** ← TDN: userDir must point to /root because Node-RED runs as root
     *  under systemd on this image. */
    userDir: '/root/.node-red',

    /** ← TDN: TDN packages are installed to /usr/share by Yocto recipes.
     *  Without nodesDir they are never discovered by Node-RED. */
    nodesDir: [
        '/usr/share/tdn-nr-base/node_modules',
        '/usr/share/tdn-nr-whatsapp/node_modules',
    ],

/*******************************************************************************
 * Security
 ******************************************************************************/

    adminAuth: {
        "type": "credentials",
        "users": [
            {
                "username": "admin",
                "password": "$2y$08$VaXgtHkzKSAW47ErgSwBfOzmg5VHzXi9aST8pUvPMbPadi5i/nFeq",
                "permissions": "*"
            }
        ]
    },

    // httpNodeAuth removed — dashboard (httpNodeRoot) is intentionally open.
    // adminAuth above protects the editor (/adminNodes).

/*******************************************************************************
 * Server Settings
 ******************************************************************************/

    /** ← TDN: port 80 — no port number needed in browser address bar */
    uiPort: 80,

    /** ← TDN: bind to all interfaces so both 127.0.0.1 and LAN IP work */
    uiHost: '0.0.0.0',

    /** ← TDN: editor at /adminNodes — password-protected via adminAuth */
    httpAdminRoot: '/adminNodes',

    httpNodeRoot: '/',

/*******************************************************************************
 * Runtime Settings
 ******************************************************************************/

    diagnostics: {
        enabled: true,
        ui: true,
    },

    runtimeState: {
        enabled: false,
        ui: false,
    },

    logging: {
        console: {
            level: "info",
            metrics: false,
            audit: false
        }
    },

    exportGlobalContextKeys: false,

    externalModules: {
    },

/*******************************************************************************
 * Editor Settings
 ******************************************************************************/

    editorTheme: {
        palette: {
        },
        projects: {
            enabled: false,
            workflow: {
                mode: "manual"
            }
        },
        codeEditor: {
            lib: "ace",
            options: {
            }
        },
        markdownEditor: {
            mermaid: {
                enabled: true
            }
        },
        multiplayer: {
            enabled: false
        },
    },

/*******************************************************************************
 * Node Settings
 ******************************************************************************/

    functionExternalModules: true,

    functionTimeout: 0,

    functionGlobalContext: {
    },

    /** ← TDN: path "" → dashboard served at / (httpNodeRoot).
     *  Kiosk URL is http://127.0.0.1/ */
    ui: { path: "" },

    debugMaxLength: 1000,

    mqttReconnectTime: 15000,

    serialReconnectTime: 15000,

}
