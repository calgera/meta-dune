SUMMARY = "DUNE: Universal Navigation Environment"
HOMEPAGE = "https://lsts.fe.up.pt/toolchain/dune"
BUGTRACKER = "https://github.com/LSTS/dune/issues"

LICENSE = "EUPL-1.0"
LIC_FILES_CHKSUM = "file://LICENCE.md;md5=1606993275ae61635cee2ffb388a69c1"
SRCREV = "4529d54a51b4ce81a69f1b48be4fbbde52025f25"
PV = "2016.03.0+git${SRCPV}"

SRC_URI = "git://github.com/LSTS/dune.git;protocol=https \
           file://0001-CMake-fix-header-install-when-PROJECT_SOURCE_DIR-inc.patch \
           file://dune.service"

S = "${WORKDIR}/git"
SYSTEMD_SERVICE_${PN} = "dune.service"

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
"

export prefix = "/opt/${PN}"
export exec_prefix = "${prefix}"

do_install_append() {
    # Install systemd service
    install -Dm0644 ${WORKDIR}/dune.service ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@BINDIR@,${bindir},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@SBINDIR@,${base_sbindir},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@SERVICEDEPS@,${DUNE_SERVICE_DEPS},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@DUNEROOT@,${prefix},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@DUNEPROFILE@,${DUNE_PROFILE},g' ${D}${systemd_system_unitdir}/dune.service
    sed -i -e 's,@DUNECFG@,${DUNE_CFG},g' ${D}${systemd_system_unitdir}/dune.service
}

FILES_${PN} += " ${prefix}/www ${prefix}/etc ${prefix}/scripts"

python () {
    if bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        if d.getVar('DUNE_PROFILE') is None:
            raise bb.parse.SkipPackage("'DUNE_PROFILE' not defined")
        if d.getVar('DUNE_CFG') is None:
            raise bb.parse.SkipPackage("'DUNE_CFG' not defined")
}
