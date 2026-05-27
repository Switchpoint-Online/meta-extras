#!/bin/bash

DEVICE="$1"
MOUNT_POINT="/mnt/usbStick"

# Get the label (optional fallback)
LABEL=$(blkid -o value -s LABEL "$DEVICE")
[ -z "$LABEL" ] && LABEL=$(basename "$DEVICE")

mkdir -p "$MOUNT_POINT"

# Mount with filesystem auto-detection
mount -o uid=1000,gid=1000 "$DEVICE" "$MOUNT_POINT" >> /tmp/mount.log 2>&1

