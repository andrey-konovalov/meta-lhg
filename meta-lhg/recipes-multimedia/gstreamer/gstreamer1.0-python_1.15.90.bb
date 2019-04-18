SUMMARY = "Python bindings for GStreamer 1.0"
HOMEPAGE = "http://cgit.freedesktop.org/gstreamer/gst-python/"
SECTION = "multimedia"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=c34deae4e395ca07e725ab0076a5f740"

SRC_URI = "http://gstreamer.freedesktop.org/src/${PNREAL}/${PNREAL}-${PV}.tar.xz"
SRC_URI[md5sum] = "c64660e440c4185fe7786250bc166b00"
SRC_URI[sha256sum] = "8a7a22c184a727fe19eaa440498571ae053bbd7b61e1fd34d2efbf762a4af8d2"

DEPENDS = "gstreamer1.0 python3-pygobject"
RDEPENDS_${PN} += "gstreamer1.0 python3-pygobject"

PNREAL = "gst-python"

S = "${WORKDIR}/${PNREAL}-${PV}"

inherit autotools pkgconfig distutils3-base upstream-version-is-even gobject-introspection

do_install_append() {
    # gstpythonplugin hardcodes the location of the libpython from the build
    # workspace and then fails at runtime. We can override it using
    # --with-libpython-dir=${libdir}, but it still fails because it looks for a
    # symlinked library ending in .so instead of the actually library with
    # LIBNAME.so.MAJOR.MINOR. Although we could patch the code to use the path
    # we want, it will break again if the library version ever changes. We need
    # to think about the best way of handling this and possibly consult
    # upstream.
    #
    # Note that this particular find line is taken from the Debian packaging for
    # gst-python1.0.
    find "${D}" \
        -name '*.pyc' -o \
        -name '*.pyo' -o \
        -name '*.la' -o \
        -name 'libgstpythonplugin*' \
        -delete
}