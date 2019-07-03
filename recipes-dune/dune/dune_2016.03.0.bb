# Copyright (C) 2019 Calgera Unipessoal Lda.
# Released under the MIT License (see COPYING.MIT for the terms)
SUMMARY = "DUNE: Universal Navigation Environment"
HOMEPAGE = "https://lsts.fe.up.pt/toolchain/dune"
BUGTRACKER = "https://github.com/LSTS/dune/issues"

LICENSE = "EUPL-1.0"
LIC_FILES_CHKSUM = "file://LICENCE.md;md5=1606993275ae61635cee2ffb388a69c1"
SRCREV_root = "4529d54a51b4ce81a69f1b48be4fbbde52025f25"
PV = "2016.03.0+git${SRCPV}"

SRC_URI = "git://github.com/LSTS/dune.git;protocol=https;name=root \
           file://0001-CMake-fix-header-install-when-PROJECT_SOURCE_DIR-inc.patch \
           file://dune.service \
           "

S = "${WORKDIR}/git"
SYSTEMD_SERVICE_${PN} = "dune.service"

DUNE_PREFIX ?= "/opt/${PN}"

inherit cmake systemd

EXTRA_OECMAKE += " \
  -DPICCOLO=no \
  -DQT5=no \
  -DDC1394=no \
  -DV4L2=no \
  -DJS=no \
  -DXENETH=no \
  -DBLUEVIEW=no \
  -DOPENCV=no \
  -DNO_RTTI=yes \
  -DUEYE=no \
  -DCMAKE_INSTALL_PREFIX:PATH=${DUNE_PREFIX} \
  -DCMAKE_INSTALL_LIBDIR:PATH=${DUNE_PREFIX}/lib \
"

do_install_append() {
    # DUNE uses the presence of the lib dir to detect if it's running
    # from the development environment
    touch ${D}${DUNE_PREFIX}/lib/.keepme

    install -Dm0644 ${WORKDIR}/dune.service ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@BINDIR@,${DUNE_PREFIX}/bin,g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@SERVICEDEPS@,${DUNE_SERVICE_DEPS},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@DUNEPROFILE@,${DUNE_PROFILE},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@DUNECFG@,${DUNE_CFG},g' ${D}${systemd_system_unitdir}/dune.service
}

FILES_${PN} += "\
    ${DUNE_PREFIX}/bin \
    ${DUNE_PREFIX}/lib/.keepme \
    ${DUNE_PREFIX}/www \
    ${DUNE_PREFIX}/etc \
    ${DUNE_PREFIX}/scripts \
    "

FILES_${PN}-staticdev += "\
    ${DUNE_PREFIX}/include \
    ${DUNE_PREFIX}/lib/libdune-core.a \
    "

python () {
    if bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        if d.getVar('DUNE_PROFILE') is None:
            raise bb.parse.SkipPackage("'DUNE_PROFILE' not defined")
        if d.getVar('DUNE_CFG') is None:
            raise bb.parse.SkipPackage("'DUNE_CFG' not defined")
}
