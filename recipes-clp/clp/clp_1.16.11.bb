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
PV = "1.16.11"
S = "${WORKDIR}/"

inherit autotools pkgconfig

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""


do_configure() {
    export CPPFLAGS='-I${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/usr/include/coin -I${CROSS_SYSROOT}/osi/${OSI_V}/image/usr/include/coin'
    export LDFLAGS='-L${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/${libdir} -L${CROSS_SYSROOT}/osi/${OSI_V}/image/${libdir}'
    export PKG_CONFIG_PATH="${PKG_CONFIG_PATH}:${CROSS_SYSROOT}/coinutils/${COINUTILS_V}/image/${libdir}/pkgconfig:${CROSS_SYSROOT}/osi/${OSI_V}/image/${libdir}/pkgconfig"
    
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

    install -m 644 ${S}/src/CbcOrClpParam.cpp ${D}${includedir}/coin
    find ${S}/src -name "*.hpp" -exec cp {} ${D}${includedir}/coin  \;
    find ${S}/src -name "*.h" -exec cp {} ${D}${includedir}/coin  \;
    

    # install lib files
    install -d ${D}${libdir}

    install -m 755 ${BUILD_DST}/src/.libs/libClp.so.0.0.0 ${D}${libdir}
    ln -s -f libClp.so.0.0.0 ${D}${libdir}/libClp.so.0
    ln -s -f libClp.so.0.0.0 ${D}${libdir}/libClp.so

    
    install -m 755 ${BUILD_DST}/src/.libs/libClpSolver.so.0.0.0 ${D}${libdir}
    ln -s -f libClpSolver.so.0.0.0 ${D}${libdir}/libClpSolver.so.0
    ln -s -f libClpSolver.so.0.0.0 ${D}${libdir}/libClpSolver.so
    chrpath -d ${D}${libdir}/libClpSolver.so

    install -m 755 ${BUILD_DST}/src/OsiClp/.libs/libOsiClp.so.0.0.0 ${D}${libdir}
    ln -s -f libOsiClp.so.0.0.0 ${D}${libdir}/libOsiClp.so.0
    ln -s -f libOsiClp.so.0.0.0 ${D}${libdir}/libOsiClp.so
    chrpath -d ${D}${libdir}/libOsiClp.so

    # install la files
    install -d ${D}${libdir}
    install -m 755 ${BUILD_DST}/src/libClp.la ${D}${libdir}
    install -c ${BUILD_DST}/src/.libs/libClp.lai ${D}${libdir}/libClp.la

    install -m 755 ${BUILD_DST}/src/libClpSolver.la ${D}${libdir}
    install -c ${BUILD_DST}/src/.libs/libClpSolver.lai ${D}${libdir}/libClpSolver.la

    install -m 755 ${BUILD_DST}/src/OsiClp/libOsiClp.la ${D}${libdir}
    install -c ${BUILD_DST}/src/OsiClp/.libs/libOsiClp.lai ${D}${libdir}/libOsiClp.la

    # install package config file (.pc)
    install -d ${D}${libdir}/pkgconfig
    install -m 644 ${BUILD_DST}/clp.pc ${D}${libdir}/pkgconfig/clp.pc
    install -m 644 ${BUILD_DST}/osi-clp.pc ${D}${libdir}/pkgconfig/osi-clp.pc
}

