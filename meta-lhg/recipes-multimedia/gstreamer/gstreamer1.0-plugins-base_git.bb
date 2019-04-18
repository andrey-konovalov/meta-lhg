require gstreamer1.0-plugins.inc

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"

SRC_URI = " \
            git://gitlab.freedesktop.org/gstreamer/gst-plugins-base;protocol=https;branch=master;name=base \
            git://gitlab.freedesktop.org/gstreamer/common;protocol=https;branch=master;destsuffix=git/common;name=common \
            file://get-caps-from-src-pad-when-query-caps.patch \
            file://0003-ssaparse-enhance-SSA-text-lines-parsing.patch \
            file://make-gio_unix_2_0-dependency-configurable.patch \
            file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
            file://0002-Makefile.am-prefix-calls-to-pkg-config-with-PKG_CONF.patch \
            file://0003-riff-add-missing-include-directories-when-calling-in.patch \
            file://0004-rtsp-drop-incorrect-reference-to-gstreamer-sdp-in-Ma.patch \
            file://0009-glimagesink-Downrank-to-marginal.patch \
            file://0001-gstreamer-gl.pc.in-don-t-append-GL_CFLAGS-to-CFLAGS.patch \
            file://link-with-libvchostif.patch \
            "
#SRC_URI[md5sum] = "83b95a7a794f386e65ad08f8c5d1efda"
#SRC_URI[sha256sum] = "6e325958526d6901546da6c1e93a990ede1772b2d6ffde1bf459fca35fea7e63"

SRCREV_base = "977aa19b8a975347d7613ea94fdbdb77a32469f6"
SRCREV_common = "32edeb4f0e665ccad767ab6a104e013522ce7e6f"
SRCREV_FORMAT = "base"

PV = "1.15.90+git${SRCPV}"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

S = "${WORKDIR}/git"

DEPENDS += "iso-codes util-linux"

inherit gettext

PACKAGES_DYNAMIC =+ "^libgst.*"

# opengl packageconfig factored out to make it easy for distros
# and BSP layers to pick either (desktop) opengl, gles2, or no GL
PACKAGECONFIG_GL ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa x11', d)} \
    gio-unix-2.0 jpeg ogg pango png theora vorbis zlib \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland egl', '', d)} \
"

X11DEPENDS = "virtual/libx11 libsm libxrender libxv"
X11ENABLEOPTS = "--enable-x --enable-xvideo --enable-xshm"
X11DISABLEOPTS = "--disable-x --disable-xvideo --disable-xshm"

PACKAGECONFIG[alsa]         = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[cdparanoia]   = "--enable-cdparanoia,--disable-cdparanoia,cdparanoia"
PACKAGECONFIG[egl]          = "--enable-egl,--disable-egl,virtual/egl"
PACKAGECONFIG[gbm]          = "--enable-gbm,--disable-gbm,virtual/libgbm libgudev libdrm"
PACKAGECONFIG[gio-unix-2.0] = "--enable-gio_unix_2_0,--disable-gio_unix_2_0,glib-2.0"
PACKAGECONFIG[gles2]        = "--enable-gles2,--disable-gles2,virtual/libgles2"
PACKAGECONFIG[ivorbis]      = "--enable-ivorbis,--disable-ivorbis,tremor"
PACKAGECONFIG[jpeg]         = "--enable-jpeg,--disable-jpeg,jpeg"
PACKAGECONFIG[ogg]          = "--enable-ogg,--disable-ogg,libogg"
PACKAGECONFIG[opengl]       = "--enable-opengl,--disable-opengl,virtual/libgl libglu"
PACKAGECONFIG[opus]         = "--enable-opus,--disable-opus,libopus"
PACKAGECONFIG[pango]        = "--enable-pango,--disable-pango,pango"
PACKAGECONFIG[png]          = "--enable-png,--disable-png,libpng"
PACKAGECONFIG[theora]       = "--enable-theora,--disable-theora,libtheora"
PACKAGECONFIG[visual]       = "--enable-libvisual,--disable-libvisual,libvisual"
PACKAGECONFIG[vorbis]       = "--enable-vorbis,--disable-vorbis,libvorbis"
PACKAGECONFIG[x11]          = "${X11ENABLEOPTS},${X11DISABLEOPTS},${X11DEPENDS}"
PACKAGECONFIG[wayland]      = "--enable-wayland,--disable-wayland,wayland-native wayland wayland-protocols libdrm"
PACKAGECONFIG[zlib]         = "--enable-zlib,--disable-zlib,zlib"

FILES_${PN}-dev += "${libdir}/gstreamer-${LIBV}/include/gst/gl/gstglconfig.h"
FILES_${MLPREFIX}libgsttag-1.0 += "${datadir}/gst-plugins-base/1.0/license-translations.dict"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}

do_compile_prepend() {
        export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/tag/.libs:${B}/gst-libs/gst/video/.libs:${B}/gst-libs/gst/audio/.libs:${B}/gst-libs/gst/rtp/.libs:${B}/gst-libs/gst/allocators/.libs"
}
