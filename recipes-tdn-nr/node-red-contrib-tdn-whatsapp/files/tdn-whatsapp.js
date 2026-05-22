'use strict';

// Guard: install once across hot reloads to catch Puppeteer teardown errors
if (!process._tdnWhatsappGuardInstalled) {
    process._tdnWhatsappGuardInstalled = true;
    process.on('uncaughtException', (err) => {
        const msg = err && err.message ? err.message : String(err);
        const isPuppeteerTeardown =
            msg.includes('Navigating frame was detached') ||
            msg.includes('Protocol error') ||
            msg.includes('Target closed') ||
            msg.includes('Session closed') ||
            msg.includes('Connection closed') ||
            msg.includes('TargetCloseError');
        if (isPuppeteerTeardown) {
            console.warn('[tdn-whatsapp] Suppressed Puppeteer teardown error:', msg);
            return;
        }
        console.error('[tdn-whatsapp] Uncaught exception:', err);
    });
}

module.exports = function (RED) {

    function ts() {
        return new Date().toTimeString().slice(0, 8);
    }

    function withTimeout(promise, ms, label) {
        return Promise.race([
            promise,
            new Promise((_, reject) =>
                setTimeout(() => reject(new Error(`${label} timed out after ${ms / 1000}s`)), ms)
            ),
        ]);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CONFIG NODE — tdn-whatsapp-config
    // ─────────────────────────────────────────────────────────────────────────
    function TdnWhatsappConfigNode(config) {
        RED.nodes.createNode(this, config);
        const node = this;

        node.authPath = config.authPath || (process.platform === 'linux'
            ? '/var/lib/tdn-whatsapp'
            : './dev-auth');
        node.verboseDebug = config.verboseDebug === true || config.verboseDebug === 'true';

        node.client = null;
        node._ready = false;
        node._qrData = null;

        const { Client, LocalAuth } = require('whatsapp-web.js');
        const qrcode = require('qrcode');

        node.log(`[config] Starting — platform=${process.platform} authPath=${node.authPath} verboseDebug=${node.verboseDebug}`);

        function initClient() {
            node._ready = false;
            node._qrData = null;
            node.log('[config] initClient() called');
            node.emit('ww-status', { type: 'init', text: `${ts()} Initialising…` });

            const isLinux = process.platform === 'linux';
            const puppeteerArgs = [
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--disable-dev-shm-usage',
                '--disable-gpu',
                '--disable-accelerated-2d-canvas',
                '--no-first-run',
                '--no-zygote',
                '--disable-extensions',
                '--disable-background-networking',
                '--disable-default-apps',
                '--disable-sync',
                '--metrics-recording-only',
                '--mute-audio',
                ...(isLinux ? ['--single-process'] : []),
            ];

            node.log(`[config] Puppeteer args: ${puppeteerArgs.join(' ')}`);

            const clientOpts = {
                authStrategy: new LocalAuth({ dataPath: node.authPath }),
                puppeteer: {
                    headless: true,
                    executablePath: isLinux ? '/usr/bin/chromium' : undefined,
                    args: puppeteerArgs,
                },
            };

            node.client = new Client(clientOpts);

            node.client.on('qr', async (qr) => {
                node.log('[config] QR received — generating data URL');
                if (node.verboseDebug) node.emit('ww-debug', { event: 'qr', ts: ts() });
                try {
                    node._qrData = await qrcode.toDataURL(qr);
                    node.log('[config] QR data URL ready');
                } catch (e) {
                    node._qrData = null;
                    node.warn(`[config] qrcode.toDataURL failed: ${e.message}`);
                }
                node.emit('ww-status', { type: 'qr', text: `${ts()} QR ready — scan at /whatsapp-qr` });
            });

            node.client.on('loading_screen', (percent, message) => {
                node.log(`[config] Loading screen: ${percent}% — ${message}`);
            });

            node.client.on('authenticated', () => {
                node.log('[config] Authenticated');
                if (node.verboseDebug) node.emit('ww-debug', { event: 'authenticated', ts: ts() });
                node.emit('ww-status', { type: 'authenticated', text: `${ts()} Authenticated` });
            });

            node.client.on('auth_failure', (msg) => {
                node.error(`[config] Auth failure: ${msg}`);
                node.emit('ww-status', { type: 'auth_failure', text: `${ts()} Auth failed: ${msg}` });
            });

            node.client.on('ready', () => {
                node._ready = true;
                node.log('[config] Client ready');
                if (node.verboseDebug) node.emit('ww-debug', { event: 'ready', ts: ts() });
                node.emit('ww-status', { type: 'ready', text: `${ts()} Ready` });
            });

            node.client.on('disconnected', (reason) => {
                node._ready = false;
                node.warn(`[config] Disconnected: ${reason}`);
                node.emit('ww-status', { type: 'disconnected', text: `${ts()} Disconnected: ${reason}` });
            });

            node.log('[config] Calling client.initialize()…');
            node.client.initialize().catch((err) => {
                node.error(`[config] initialize() error: ${err.message}`);
                node.emit('ww-status', { type: 'error', text: `${ts()} Init error: ${err.message}` });
            });
        }

        // QR page
        RED.httpAdmin.get('/whatsapp-qr', RED.auth.needsPermission('flows.read'), (req, res) => {
            if (node._qrData) {
                res.send(`<!DOCTYPE html>
<html><head><title>WhatsApp QR</title>
<meta http-equiv="refresh" content="15">
<style>body{font-family:sans-serif;text-align:center;padding:40px;background:#f0f0f0}
img{border:8px solid white;border-radius:12px;box-shadow:0 4px 20px rgba(0,0,0,.2)}</style>
</head><body>
<h2>TDN WhatsApp — Scan QR</h2>
<img src="${node._qrData}" alt="QR Code" width="300" height="300"><br>
<p style="color:#888">Page auto-refreshes every 15 s</p>
</body></html>`);
            } else if (node._ready) {
                res.send(`<!DOCTYPE html><html><head><title>WhatsApp QR</title></head>
<body style="font-family:sans-serif;text-align:center;padding:40px">
<h2>Already authenticated &amp; ready</h2></body></html>`);
            } else {
                res.send(`<!DOCTYPE html><html><head><title>WhatsApp QR</title>
<meta http-equiv="refresh" content="5"></head>
<body style="font-family:sans-serif;text-align:center;padding:40px">
<h2>Waiting for QR…</h2><p>Page auto-refreshes every 5 s</p></body></html>`);
            }
        });

        initClient();

        node.on('close', async (done) => {
            node.log('[config] Closing — destroying client');
            if (node.client) {
                try {
                    await node.client.destroy();
                    node.log('[config] Client destroyed');
                } catch (e) {
                    node.warn(`[config] destroy() swallowed: ${e.message}`);
                }
                node.client = null;
            }
            node._ready = false;
            done();
        });
    }

    RED.nodes.registerType('tdn-whatsapp-config', TdnWhatsappConfigNode);

    // ─────────────────────────────────────────────────────────────────────────
    // SEND NODE — tdn-whatsapp-send
    // ─────────────────────────────────────────────────────────────────────────
    function TdnWhatsappSendNode(config) {
        RED.nodes.createNode(this, config);
        const node = this;

        node.configNode = RED.nodes.getNode(config.server);
        node.defaultTo  = config.defaultTo || '';

        node.status({ fill: 'grey', shape: 'dot', text: `${ts()} Idle` });

        if (node.configNode) {
            node.configNode.on('ww-status', (s) => {
                const fill  = s.type === 'ready'         ? 'green'
                            : s.type === 'qr'            ? 'yellow'
                            : s.type === 'init'          ? 'grey'
                            : s.type === 'authenticated' ? 'blue'
                            : 'red';
                const shape = (s.type === 'qr' || s.type === 'error' || s.type === 'disconnected' || s.type === 'auth_failure')
                            ? 'ring' : 'dot';
                node.status({ fill, shape, text: s.text });
            });

            node.configNode.on('ww-debug', (d) => {
                if (node.configNode.verboseDebug) {
                    node.send([null, { payload: d, topic: 'ww-debug' }]);
                }
            });
        }

        node.on('input', async (msg, send, done) => {
            if (!node.configNode || !node.configNode._ready) {
                node.warn('[send] Input received but client not ready');
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} Not ready` });
                done(new Error('WhatsApp client not ready'));
                return;
            }

            const text      = msg.payload != null ? String(msg.payload) : '';
            const recipient = msg.to || node.defaultTo;

            if (!recipient) {
                node.warn('[send] No recipient — set msg.to or configure a default');
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} No recipient` });
                done(new Error('No recipient — set msg.to or configure a default'));
                return;
            }

            node.log(`[send] Sending to ${recipient} — "${text.slice(0, 40)}${text.length > 40 ? '…' : ''}"`);
            node.status({ fill: 'blue', shape: 'dot', text: `${ts()} Sending…` });

            try {
                const result = await withTimeout(
                    node.configNode.client.sendMessage(recipient, text),
                    30000, 'sendMessage'
                );
                node.log(`[send] Sent OK — messageId=${result.id._serialized}`);
                node.status({ fill: 'green', shape: 'dot', text: `${ts()} Sent` });
                send([{ payload: { success: true, to: recipient, messageId: result.id._serialized }, topic: 'sent' }, null]);
                done();
            } catch (err) {
                node.error(`[send] sendMessage failed: ${err.message}`);
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} ${err.message}` });
                done(err);
            }
        });

        node.on('close', (done) => {
            node.status({});
            done();
        });
    }

    RED.nodes.registerType('tdn-whatsapp-send', TdnWhatsappSendNode);

    // ─────────────────────────────────────────────────────────────────────────
    // CONTACTS NODE — tdn-whatsapp-contacts
    // ─────────────────────────────────────────────────────────────────────────
    function TdnWhatsappContactsNode(config) {
        RED.nodes.createNode(this, config);
        const node = this;

        node.configNode = RED.nodes.getNode(config.server);

        node.status({ fill: 'grey', shape: 'dot', text: `${ts()} Idle` });

        if (node.configNode) {
            node.configNode.on('ww-status', (s) => {
                const fill  = s.type === 'ready'         ? 'green'
                            : s.type === 'qr'            ? 'yellow'
                            : s.type === 'init'          ? 'grey'
                            : s.type === 'authenticated' ? 'blue'
                            : 'red';
                const shape = (s.type === 'qr' || s.type === 'error' || s.type === 'disconnected' || s.type === 'auth_failure')
                            ? 'ring' : 'dot';
                node.status({ fill, shape, text: s.text });
            });

            node.configNode.on('ww-debug', (d) => {
                if (node.configNode.verboseDebug) {
                    node.send([null, { payload: d, topic: 'ww-debug' }]);
                }
            });
        }

        node.on('input', async (msg, send, done) => {
            if (!node.configNode || !node.configNode._ready) {
                node.warn('[contacts] Input received but client not ready');
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} Not ready` });
                done(new Error('WhatsApp client not ready'));
                return;
            }

            node.log('[contacts] getChats() starting (30s timeout)');
            node.status({ fill: 'blue', shape: 'dot', text: `${ts()} Fetching chats…` });

            try {
                const chats = await withTimeout(
                    node.configNode.client.getChats(),
                    30000, 'getChats'
                );

                node.log(`[contacts] getChats() returned ${chats.length} chats`);

                const users = chats
                    .filter(c => !c.isGroup)
                    .map(c => ({
                        id:        c.id._serialized,
                        name:      c.name || c.id.user,
                        number:    c.id.user || '',
                        unread:    c.unreadCount || 0,
                        timestamp: c.timestamp   || 0,
                    }))
                    .sort((a, b) => a.name.localeCompare(b.name));

                const groups = chats
                    .filter(c => c.isGroup)
                    .map(c => ({
                        id:           c.id._serialized,
                        name:         c.name,
                        participants: c.groupMetadata?.participants?.length || 0,
                        unread:       c.unreadCount || 0,
                        timestamp:    c.timestamp   || 0,
                    }))
                    .sort((a, b) => a.name.localeCompare(b.name));

                node.log(`[contacts] ${users.length} users, ${groups.length} groups`);
                node.status({ fill: 'green', shape: 'dot', text: `${ts()} ${users.length} users, ${groups.length} groups` });
                send([{ payload: { users, groups }, topic: 'contacts' }, null]);
                done();
            } catch (err) {
                node.error(`[contacts] getChats() failed: ${err.message}`);
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} ${err.message}` });
                done(err);
            }
        });

        node.on('close', (done) => {
            node.status({});
            done();
        });
    }

    RED.nodes.registerType('tdn-whatsapp-contacts', TdnWhatsappContactsNode);

    // ─────────────────────────────────────────────────────────────────────────
    // ALARM NODE — tdn-whatsapp-alarm
    // Rate-limited send for alarm events (default max 10/day).
    // Resets the counter at midnight UTC.
    // ─────────────────────────────────────────────────────────────────────────
    function TdnWhatsappAlarmNode(config) {
        RED.nodes.createNode(this, config);
        const node = this;

        node.configNode  = RED.nodes.getNode(config.server);
        node.defaultTo   = config.defaultTo  || '';
        node.defaultMsg  = config.defaultMsg  || 'ALARM TRIGGERED';
        node.dailyLimit  = parseInt(config.dailyLimit, 10) || 10;

        node._sentToday = 0;
        node._dayStamp  = new Date().toDateString();

        node.status({ fill: 'grey', shape: 'dot', text: `${ts()} Idle` });

        if (node.configNode) {
            node.configNode.on('ww-status', (s) => {
                const fill  = s.type === 'ready'         ? 'green'
                            : s.type === 'qr'            ? 'yellow'
                            : s.type === 'init'          ? 'grey'
                            : s.type === 'authenticated' ? 'blue'
                            : 'red';
                const shape = (s.type === 'qr' || s.type === 'error' || s.type === 'disconnected' || s.type === 'auth_failure')
                            ? 'ring' : 'dot';
                node.status({ fill, shape, text: s.text });
            });
        }

        node.on('input', async (msg, send, done) => {
            const today = new Date().toDateString();
            if (today !== node._dayStamp) {
                node._sentToday = 0;
                node._dayStamp  = today;
            }

            if (!node.configNode || !node.configNode._ready) {
                node.warn('[alarm] Client not ready');
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} Not ready` });
                done(new Error('WhatsApp client not ready'));
                return;
            }

            if (node._sentToday >= node.dailyLimit) {
                node.warn(`[alarm] Daily limit (${node.dailyLimit}) reached — message suppressed`);
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} Daily limit reached` });
                done(new Error(`Daily limit of ${node.dailyLimit} reached`));
                return;
            }

            const text      = msg.payload != null ? String(msg.payload) : node.defaultMsg;
            const recipient = msg.to || node.defaultTo;

            if (!recipient) {
                node.warn('[alarm] No recipient');
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} No recipient` });
                done(new Error('No recipient — set msg.to or configure a default'));
                return;
            }

            node.log(`[alarm] Sending to ${recipient} (${node._sentToday + 1}/${node.dailyLimit} today)`);
            node.status({ fill: 'blue', shape: 'dot', text: `${ts()} Sending…` });

            try {
                const result = await withTimeout(
                    node.configNode.client.sendMessage(recipient, text),
                    30000, 'sendMessage'
                );
                node._sentToday++;
                node.log(`[alarm] Sent OK — messageId=${result.id._serialized}`);
                node.status({ fill: 'green', shape: 'dot', text: `${ts()} Sent (${node._sentToday}/${node.dailyLimit})` });
                send([{ payload: { success: true, to: recipient, messageId: result.id._serialized, sentToday: node._sentToday }, topic: 'alarm-sent' }, null]);
                done();
            } catch (err) {
                node.error(`[alarm] sendMessage failed: ${err.message}`);
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} ${err.message}` });
                done(err);
            }
        });

        node.on('close', (done) => {
            node.status({});
            done();
        });
    }

    RED.nodes.registerType('tdn-whatsapp-alarm', TdnWhatsappAlarmNode);

    // ─────────────────────────────────────────────────────────────────────────
    // SCHEDULER NODE — tdn-whatsapp-scheduler
    // Fires once per week on a configured day/time and sends a proof-of-life
    // message. Polls every 30 s; tracks last-fired date to prevent double-fire.
    // ─────────────────────────────────────────────────────────────────────────
    function TdnWhatsappSchedulerNode(config) {
        RED.nodes.createNode(this, config);
        const node = this;

        node.configNode = RED.nodes.getNode(config.server);
        node.recipient  = config.recipient || '';
        node.message    = config.message   || 'Proof of life — system OK';
        node.dayOfWeek  = parseInt(config.dayOfWeek, 10) || 0;  // 0=Sun … 6=Sat
        node.hour       = parseInt(config.hour,      10) || 9;
        node.minute     = parseInt(config.minute,    10) || 0;

        node._lastFired = null;   // 'YYYY-MM-DD' of last successful send
        node._timer     = null;

        const DAY_NAMES = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

        function nextFireLabel() {
            const now  = new Date();
            const diff = (node.dayOfWeek - now.getDay() + 7) % 7 ||
                (now.getHours() > node.hour ||
                 (now.getHours() === node.hour && now.getMinutes() >= node.minute) ? 7 : 0);
            const next = new Date(now);
            next.setDate(now.getDate() + diff);
            next.setHours(node.hour, node.minute, 0, 0);
            return `${DAY_NAMES[next.getDay()]} ${String(node.hour).padStart(2, '0')}:${String(node.minute).padStart(2, '0')}`;
        }

        function updateStatus() {
            if (node.configNode && node.configNode._ready) {
                node.status({ fill: 'green', shape: 'dot', text: `Next: ${nextFireLabel()}` });
            } else {
                node.status({ fill: 'grey', shape: 'ring', text: `Next: ${nextFireLabel()}` });
            }
        }

        async function fire() {
            if (!node.configNode || !node.configNode._ready) {
                node.warn('[scheduler] Not ready — scheduled send skipped');
                return;
            }
            if (!node.recipient) {
                node.warn('[scheduler] No recipient configured');
                return;
            }

            node.log(`[scheduler] Firing weekly message to ${node.recipient}`);
            node.status({ fill: 'blue', shape: 'dot', text: `${ts()} Sending…` });

            try {
                const result = await withTimeout(
                    node.configNode.client.sendMessage(node.recipient, node.message),
                    30000, 'sendMessage'
                );
                node.log(`[scheduler] Sent OK — messageId=${result.id._serialized}`);
                node.send({ payload: { success: true, to: node.recipient, messageId: result.id._serialized, message: node.message }, topic: 'scheduled' });
                updateStatus();
            } catch (err) {
                node.error(`[scheduler] sendMessage failed: ${err.message}`);
                node.status({ fill: 'red', shape: 'ring', text: `${ts()} ${err.message}` });
            }
        }

        function tick() {
            const now   = new Date();
            const today = now.toISOString().slice(0, 10);

            if (now.getDay()     !== node.dayOfWeek) return;
            if (now.getHours()   !== node.hour)      return;
            if (now.getMinutes() !== node.minute)    return;
            if (node._lastFired  === today)          return;  // already fired this occurrence

            node._lastFired = today;
            fire();
        }

        if (node.configNode) {
            node.configNode.on('ww-status', updateStatus);
        }

        node._timer = setInterval(tick, 30000);
        updateStatus();

        node.on('close', (done) => {
            if (node._timer) { clearInterval(node._timer); node._timer = null; }
            node.status({});
            done();
        });
    }

    RED.nodes.registerType('tdn-whatsapp-scheduler', TdnWhatsappSchedulerNode);
};
