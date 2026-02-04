# Make sure Mesa builds the X11/DRI3 path and pulls xshmfence in
PACKAGECONFIG:append = " x11 dri3"

# (belt-and-suspenders) ensure the .pc is present during do_configure
DEPENDS:append = " libxshmfence"