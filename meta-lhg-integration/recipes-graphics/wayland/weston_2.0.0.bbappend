# When configured for fbdev compositor, make it the default
PACKAGECONFIG[fbdev] := "--enable-fbdev-compositor ${@bb.utils.contains('MACHINE_FEATURES', 'sgx', 'WESTON_NATIVE_BACKEND="fbdev-backend.so"', '', d)},--disable-fbdev-compositor,udev mtdev"
PACKAGECONFIG[kms] := "--enable-drm-compositor,--disable-drm-compositor,drm udev ${@bb.utils.contains('MACHINE_FEATURES', 'sgx', 'libgbm', 'virtual/mesa', d)} mtdev"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_omap-a15 = " \
"

#	file://0001-udev-seat-restrict-udev-enumeration-to-card0.patch
#	file://0002-Weston-Allow-visual_id-to-be-0.patch
#	file://0003-Weston-Fix-virtual-keyboard-display-issue-for-QT5-ap.patch
#	file://0004-Weston-Fix-touch-screen-crash-issue.patch
#	file://0001-compositor-drm-fix-hotplug-weston-termination-proble.patch
#	file://0001-compositor-drm-support-RGB565-with-pixman-renderer.patch
