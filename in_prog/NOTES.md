# in_prog — legacy recipes archived from meta-extras

BitBake does NOT scan this directory (pattern requires `recipes-*/`).

## Moved directories

| Directory | Was in | Replaced by |
|---|---|---|
| `node-red-contrib-tdn-whatsapp/` | `recipes-tdn-nr/` | `tdn-nr-whatsapp` + `tdn-nodered-base` |
| `tdn-nr-bcm2711-Scarthgap/` | `recipes-tdn-nr/` | `tdn-nr-report` + `tdn-nr-dahua` + `tdn-nodered-base` |
| `tdn-procscan/` | `recipes-tdn-nr/` | `tdn-nodered-base` (procscan compiled there now) |
| `tdn-nr-am3358/` | `recipes-tdn-nr/` | BeagleBone legacy — not used |
| `tdn-nr-ews-chromium/` | `recipes-tdn-nr/` | Empty stub — superseded |

## Reference tarballs (do not delete)

- `node-red-contrib-tdn-whatsapp/nr-files/tdn-ewsv3-3.0.1.tgz` — old EWS full bundle
- `tdn-nr-bcm2711-Scarthgap/nr-files/tdn-eth-dual_v3.2.8.tgz` — old Eth bundle
- `tdn-nr-bcm2711-Scarthgap/nr-files/tdn-ethv3-3.1.0.tgz` — old Eth bundle v2

## local.conf

`local.conf` still references old recipe names. After the current build completes,
update `IMAGE_INSTALL` to use: `tdn-nodered-base tdn-nr-whatsapp` (EWS) or
`tdn-nodered-base tdn-nr-report tdn-nr-dahua` (Eth). See CLAUDE_PROPOSAL.md.
