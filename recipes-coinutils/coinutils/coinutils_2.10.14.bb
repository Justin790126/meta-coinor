# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=44f12365016145dc45da7854fe1d6c8e"

SRC_URI = "file://*"

# Modify these as desired
PV = "2.10.14"

S = "${WORKDIR}/"

inherit autotools pkgconfig

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECONF = ""

do_configure() {
    ${S}/configure --target=${TARGET_SYS} \
    --host=${TARGET_SYS} \
    --with-sysroot=${sysroot_dir} \
    --libdir=${libdir} \
    --prefix=/usr
}

do_compile() {
    oe_runmake
}

do_install() {
    BUILD_DST=${WORKDIR}/build
    install -d ${D}${includedir}/coin

    find ${BUILD_DST}/src -name "*.h" -exec cp {} ${D}${includedir}/coin  \;
    find ${S}/src -name "*.hpp" -exec cp {} ${D}${includedir}/coin  \;
    find ${S}/src -name "*.h" -exec cp {} ${D}${includedir}/coin  \;

    install -d ${D}${libdir}
    install -m 755 ${BUILD_DST}/src/.libs/libCoinUtils.so.0.0.0 ${D}${libdir}

    install -d ${D}${libdir}
    install -m 755 ${BUILD_DST}/src/libCoinUtils.la ${D}${libdir}

    install -d ${D}${libdir}
    install -c ${BUILD_DST}/src/.libs/libCoinUtils.lai ${D}${libdir}/libCoinUtils.la

    ln -s -f libCoinUtils.so.0.0.0 ${D}${libdir}/libCoinUtils.so.0
    ln -s -f libCoinUtils.so.0.0.0 ${D}${libdir}/libCoinUtils.so

    install -D -m 644 ${BUILD_DST}/coinutils.pc ${D}${libdir}/pkgconfig/coinutils.pc
}
