SUMMARY = "Libav-based GStreamer 1.x plugin"
HOMEPAGE = "http://gstreamer.freedesktop.org/"
SECTION = "multimedia"

LICENSE = "GPLv2+ & LGPLv2+ & ( (GPLv2+ & LGPLv2.1+) | (GPLv3+ & LGPLv3+) )"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                    file://ext/libav/gstav.h;beginline=1;endline=18;md5=a752c35267d8276fd9ca3db6994fca9c \
                    file://gst-libs/ext/libav/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://gst-libs/ext/libav/COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://gst-libs/ext/libav/COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://gst-libs/ext/libav/COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = "git://gitlab.freedesktop.org/gstreamer/gst-libav;protocol=https;branch=master;name=base \
           git://gitlab.freedesktop.org/gstreamer/common;protocol=https;branch=master;destsuffix=git/common;name=common \
           git://git.videolan.org/git/ffmpeg.git;protocol=https;branch=release/4.1;destsuffix=git/gst-libs/ext/libav;name=ffmpeg \
           file://0001-Disable-yasm-for-libav-when-disable-yasm.patch \
           file://workaround-to-build-gst-libav-for-i586-with-gcc.patch \
           file://mips64_cpu_detection.patch \
           file://0001-configure-check-for-armv7ve-variant.patch \
           file://0001-fix-host-contamination.patch \
           "
#SRC_URI[md5sum] = "f8aaeed8baeae147ea4178f7611db193"
#SRC_URI[sha256sum] = "93eacda6327ee4fcbb3254e68757d555196d1e2c689e1b79a46e28f33ef20543"

SRCREV_base = "ef8a1bdd90daa04e9022561a8c338f1a23ee4bdc"
SRCREV_common = "9b2a1d676f77f39d25d6674c43b858293b4b0a19"
SRCREV_ffmpeg = "74700e50bf7444930bfc12935bd3e17cd5f766c1"
SRCREV_FORMAT = "base"
PV = "1.15.2+git${SRCPV}"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

S = "${WORKDIR}/git"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base zlib bzip2 xz"

inherit autotools pkgconfig upstream-version-is-even gtk-doc

# CAUTION: Using the system libav is not recommended. Since the libav API is changing all the time,
# compilation errors (and other, more subtle bugs) can happen. It is usually better to rely on the
# libav copy included in the gst-libav package.
PACKAGECONFIG ??= "orc yasm"

PACKAGECONFIG[gpl] = "--enable-gpl,--disable-gpl,"
PACKAGECONFIG[libav] = "--with-system-libav,,libav"
PACKAGECONFIG[orc] = "--enable-orc,--disable-orc,orc"
PACKAGECONFIG[yasm] = "--enable-yasm,--disable-yasm,nasm-native"
PACKAGECONFIG[valgrind] = "--enable-valgrind,--disable-valgrind,valgrind"

GSTREAMER_1_0_DEBUG ?= "--disable-debug"

LIBAV_EXTRA_CONFIGURE = "--with-libav-extra-configure"

LIBAV_EXTRA_CONFIGURE_COMMON_ARG = "--target-os=linux \
  --cc='${CC}' --as='${CC}' --ld='${CC}' --nm='${NM}' --ar='${AR}' \
  --ranlib='${RANLIB}' \
  ${GSTREAMER_1_0_DEBUG} \
  --cross-prefix='${HOST_PREFIX}'"

# Disable assembly optimizations for X32, as this libav lacks the support
PACKAGECONFIG_remove_linux-gnux32 = "yasm"
LIBAV_EXTRA_CONFIGURE_COMMON_ARG_append_linux-gnux32 = " --disable-asm"

LIBAV_EXTRA_CONFIGURE_COMMON = \
'${LIBAV_EXTRA_CONFIGURE}="${LIBAV_EXTRA_CONFIGURE_COMMON_ARG}"'

EXTRA_OECONF = "${LIBAV_EXTRA_CONFIGURE_COMMON}"

FILES_${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"

# http://errors.yoctoproject.org/Errors/Details/20493/
ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

# ffmpeg/libav disables PIC on some platforms (e.g. x86-32)
INSANE_SKIP_${PN} = "textrel"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}