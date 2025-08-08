# #!/bin/sh
# #
# # SPDX-License-Identifier: GPL-2.0-or-later
# #

# set -e

# imageh=`basename $1 .png`-img.h
# name="${2}_IMG"
# gdk-pixbuf-csource --macros $1 > $imageh.tmp
# sed -e "s/MY_PIXBUF/${name}/g" -e "s/guint8/uint8/g" $imageh.tmp > $imageh && rm $imageh.tmp


#!/bin/bash
set -e

if [ $# -ne 2 ]; then
    echo "Usage: $0 <image.png> <NAME>"
    exit 1
fi

img_file="$1"
symbol_name="$2"

img_basename=$(basename "$img_file" .png)
output_file="${img_basename}-img.h"
macro_name="${symbol_name}_IMG"

echo "Generating $output_file with symbol $macro_name"

gdk-pixbuf-csource --raw --name="${macro_name}" "$img_file" > "${output_file}"
