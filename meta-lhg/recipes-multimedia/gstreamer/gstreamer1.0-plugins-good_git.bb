require gstreamer1.0-plugins.inc

SRC_URI = " \
            git://gitlab.freedesktop.org/gstreamer/gst-plugins-good;protocol=https;branch=master;name=base \
            git://gitlab.freedesktop.org/gstreamer/common;protocol=https;branch=master;destsuffix=git/common;name=common \
            file://0001-gstrtpmp4gpay-set-dafault-value-for-MPEG4-without-co.patch \
            file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
            "

#SRC_URI[md5sum] = "0101e2b18bb1cb71d8aa83aef8363361"
#SRC_URI[sha256sum] = "2b9fb4c5514981e4f75aa1af20a34bac72a447885763d03da3d23c7e329dc695"

SRCREV_base = "646dc1ef9b92841e235d10a8262882b6e7d97422"
SRCREV_common = "32edeb4f0e665ccad767ab6a104e013522ce7e6f"
SRCREV_FORMAT = "base"

PV = "1.16.0+git${SRCPV}"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

S = "${WORKDIR}/git"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

DEPENDS += "gstreamer1.0-plugins-base libcap"
RPROVIDES_${PN}-pulseaudio += "${PN}-pulse"
RPROVIDES_${PN}-soup += "${PN}-souphttpsrc"

inherit gettext

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pulseaudio x11', d)} \
    bz2 cairo flac gdk-pixbuf gudev jpeg lame libpng mpg123 soup speex taglib v4l2 zlib \
"

X11DEPENDS = "virtual/libx11 libsm libxrender libxfixes libxdamage"

PACKAGECONFIG[bz2]        = "--enable-bz2,--disable-bz2,bzip2"
PACKAGECONFIG[cairo]      = "--enable-cairo,--disable-cairo,cairo"
PACKAGECONFIG[dv1394]     = "--enable-dv1394,--disable-dv1394,libiec61883 libavc1394 libraw1394"
PACKAGECONFIG[flac]       = "--enable-flac,--disable-flac,flac"
PACKAGECONFIG[gdk-pixbuf] = "--enable-gdk_pixbuf,--disable-gdk_pixbuf,gdk-pixbuf"
PACKAGECONFIG[gtk]        = "--enable-gtk3,--disable-gtk3,gtk+3"
PACKAGECONFIG[gudev]      = "--with-gudev,--without-gudev,libgudev"
PACKAGECONFIG[jack]       = "--enable-jack,--disable-jack,jack"
PACKAGECONFIG[jpeg]       = "--enable-jpeg,--disable-jpeg,jpeg"
PACKAGECONFIG[lame]       = "--enable-lame,--disable-lame,lame"
PACKAGECONFIG[libpng]     = "--enable-libpng,--disable-libpng,libpng"
PACKAGECONFIG[libv4l2]    = "--with-libv4l2,--without-libv4l2,v4l-utils"
PACKAGECONFIG[mpg123]     = "--enable-mpg123,--disable-mpg123,mpg123"
PACKAGECONFIG[pulseaudio] = "--enable-pulse,--disable-pulse,pulseaudio"
PACKAGECONFIG[soup]       = "--enable-soup,--disable-soup,libsoup-2.4"
PACKAGECONFIG[speex]      = "--enable-speex,--disable-speex,speex"
PACKAGECONFIG[taglib]     = "--enable-taglib,--disable-taglib,taglib"
PACKAGECONFIG[v4l2]       = "--enable-gst_v4l2 --enable-v4l2-probe,--disable-gst_v4l2"
PACKAGECONFIG[vpx]        = "--enable-vpx,--disable-vpx,libvpx"
PACKAGECONFIG[wavpack]    = "--enable-wavpack,--disable-wavpack,wavpack"
PACKAGECONFIG[x11]        = "--enable-x,--disable-x,${X11DEPENDS}"
PACKAGECONFIG[zlib]       = "--enable-zlib,--disable-zlib,zlib"

# qt5 support is disabled, because it is not present in OE core, and requires more work than
# just adding a packageconfig (it requires access to moc, uic, rcc, and qmake paths).
# This is better done in a separate qt5 layer (which then should add a "qt5" packageconfig
# in a gstreamer1.0-plugins-good bbappend).

EXTRA_OECONF += " \
    --enable-oss \
    --disable-aalib \
    --disable-aalibtest \
    --disable-directsound \
    --disable-libcaca \
    --disable-libdv \
    --disable-oss4 \
    --disable-osx_audio \
    --disable-osx_video \
    --disable-qt \
    --disable-shout2 \
    --disable-twolame \
    --disable-waveform \
"

FILES_${PN}-equalizer += "${datadir}/gstreamer-1.0/presets/*.prs"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}
