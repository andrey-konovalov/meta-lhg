require gstreamer1.0-plugins.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

LICENSE = "GPLv2+ & LGPLv2.1+ & LGPLv2+"
LICENSE_FLAGS = "commercial"

SRC_URI = " \
            http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
            file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
            "
SRC_URI[md5sum] = "142530d07aae02f326c1a0af85fe8941"
SRC_URI[sha256sum] = "178fa922cf54a1eb1c563e1a1b272c9b5dbf50fb331830170ddf2aeec1e8e99e"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"

DEPENDS += "gstreamer1.0-plugins-base libid3tag"

inherit gettext

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    a52dec mpeg2dec \
"

PACKAGECONFIG[a52dec]   = "--enable-a52dec,--disable-a52dec,liba52"
PACKAGECONFIG[amrnb]    = "--enable-amrnb,--disable-amrnb,opencore-amr"
PACKAGECONFIG[amrwb]    = "--enable-amrwb,--disable-amrwb,opencore-amr"
PACKAGECONFIG[cdio]     = "--enable-cdio,--disable-cdio,libcdio"
PACKAGECONFIG[dvdread]  = "--enable-dvdread,--disable-dvdread,libdvdread"
PACKAGECONFIG[mpeg2dec] = "--enable-mpeg2dec,--disable-mpeg2dec,mpeg2dec"
PACKAGECONFIG[x264]     = "--enable-x264,--disable-x264,x264"

EXTRA_OECONF += " \
    --disable-sidplay \
"

FILES_${PN}-amrnb += "${datadir}/gstreamer-1.0/presets/GstAmrnbEnc.prs"
FILES_${PN}-x264 += "${datadir}/gstreamer-1.0/presets/GstX264Enc.prs"
