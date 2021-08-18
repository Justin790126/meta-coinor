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
PV = "0.59.10"

S = "${WORKDIR}/"

inherit autotools pkgconfig

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""

do_configure() {
    export CPPFLAGS='-I${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/usr/include/coin -I${CROSS_SYSROOT}/osi/${OSI_V}/image/usr/include/coin -I${CROSS_SYSROOT}/clp/${CLP_V}/image/usr/include/coin'
    export LDFLAGS='-L${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/${libdir} -L${CROSS_SYSROOT}/osi/${OSI_V}/image/${libdir} -L${CROSS_SYSROOT}/clp/${CLP_V}/image/${libdir}'
    export PKG_CONFIG_PATH="${PKG_CONFIG_PATH}:${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/${libdir}/pkgconfig:${TMPDIR}/work/${TARGET_SYS}/osi/${OSI_V}/image/${libdir}/pkgconfig:${CROSS_SYSROOT}/clp/${CLP_V}/image/${libdir}/pkgconfig"
    
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
    
    # install include files
    install -d ${D}${includedir}/coin

    find ${BUILD_DST}/src -name "*.h" -exec cp {} ${D}${includedir}/coin  \;
    find ${S}/src -name "*.hpp" -exec cp {} ${D}${includedir}/coin  \;
    find ${S}/src -name "*.h" -exec cp {} ${D}${includedir}/coin  \;


    # install lib files
    install -d ${D}${libdir}
    install -m 755 ${BUILD_DST}/src/CglCommon/.libs/libCgl.so.0.0.0 ${D}${libdir}
    ln -s -f libCgl.so.0.0.0 ${D}${libdir}/libCgl.so.0
    ln -s -f libCgl.so.0.0.0 ${D}${libdir}/libCgl.so


    # install package config file (.pc)
    install -d ${D}${libdir}/pkgconfig
    install -m 644 ${BUILD_DST}/cgl.pc ${D}${libdir}/pkgconfig/cgl.pc
}