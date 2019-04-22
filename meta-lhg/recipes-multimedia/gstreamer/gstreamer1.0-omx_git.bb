SUMMARY = "OpenMAX IL plugins for GStreamer"
HOMEPAGE = "http://gstreamer.freedesktop.org/"
SECTION = "multimedia"

LICENSE = "LGPLv2.1"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

#SRC_URI = "http://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"
SRC_URI = "git://gitlab.freedesktop.org/gstreamer/gst-omx;protocol=https;branch=master;name=gst-omx \
           git://gitlab.freedesktop.org/gstreamer/common;protocol=https;branch=master;destsuffix=git/common;name=common \"

#SRC_URI[md5sum] = "0655aec006f713279742df26a8c640bd"
#SRC_URI[sha256sum] = "b4313731939b23359201177770c694cfb64556583453d7bf9f28453aa95c2d6f"

SRCREV_gst-omx = "efb55bfa9fac024efea491895f6bf486a712be7e"
SRCREV_common = "32edeb4f0e665ccad767ab6a104e013522ce7e6f"
SRCREV_FORMAT = "gst-omx"
PV = "1.16.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"

inherit autotools pkgconfig gettext gtk-doc upstream-version-is-even

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

GSTREAMER_1_0_OMX_TARGET ?= "bellagio"
GSTREAMER_1_0_OMX_CORE_NAME ?= "${libdir}/libomxil-bellagio.so.0"

EXTRA_OECONF += "--disable-valgrind --with-omx-target=${GSTREAMER_1_0_OMX_TARGET}"

python __anonymous () {
    omx_target = d.getVar("GSTREAMER_1_0_OMX_TARGET")
    if omx_target in ['generic', 'bellagio']:
        # Bellagio headers are incomplete (they are missing the OMX_VERSION_MAJOR,#
        # OMX_VERSION_MINOR, OMX_VERSION_REVISION, and OMX_VERSION_STEP macros);
        # appending a directory path to gst-omx' internal OpenMAX IL headers fixes this
        d.appendVar("CFLAGS", " -I${S}/omx/openmax")
    elif omx_target == "rpi":
        # Dedicated Raspberry Pi OpenMAX IL support makes this package machine specific
        d.setVar("PACKAGE_ARCH", d.getVar("MACHINE_ARCH"))
}

delete_pkg_m4_file() {
    # Delete m4 files which we provide patched versions of but will be ignored
    # if these exist
	rm -f "${S}/common/m4/pkg.m4"
	rm -f "${S}/common/m4/gtk-doc.m4"
}
do_configure[prefuncs] += "delete_pkg_m4_file"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

set_omx_core_name() {
	sed -i -e "s;^core-name=.*;core-name=${GSTREAMER_1_0_OMX_CORE_NAME};" "${D}${sysconfdir}/xdg/gstomx.conf"
}
do_install[postfuncs] += " set_omx_core_name "

FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"

RDEPENDS_${PN} = "libomxil"
