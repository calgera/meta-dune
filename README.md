Yocto layer for the DUNE: Universal Navigation Environment
=========================================================

This layer provides support for DUNE for use with OpenEmbedded and/or Yocto.

DUNE: Unified Navigation Environment is a runtime environment for unmanned
systems on-board software. Please see <https://lsts.fe.up.pt/toolchain/dune>.

Contributing
------------

Please submit any patches against the `meta-dune` layer by using the GitHub
pull-request feature. When submitting patches please refer to:
http://openembedded.org/wiki/Commit_Patch_Message_Guidelines

Source code:
    https://github.com/calgera/meta-dune

This layer is maintained by Renato Caldas: <renato@calgera.com>.

Dependencies
------------

This layer depends on:

URI: git://git.openembedded.org/openembedded-core
branch: master
revision: HEAD

Customization
-------------

DUNE is a framework, and you will probably need to tailor it for your own
unmanned systems. The `dune` recipe included in this layer is kept as simple as
possible, which means that if you need to customize DUNE in any way, you'll have
to modify the recipe. The simplest way to do this is with a `*.bbappend` recipe.

### User Tasks and configurations

The most common scenario is when you have your own Tasks and configurations.
DUNE supports a `user` subdirectory at the root of the source tree. As an
example, to fetch this subdirectory from github you can put this in your
`*.bbappend`:

```
SRC_URI += "git://github.com/myaccount/myrepo.git;destsuffix=git/user;name=user \"
           "
SRCREV_user = "your_repo_revision_hash_here"
SRCREV_FORMAT = "root_user"

```

Please note that setting `SRCREV_FORMAT` is required in this case, as it defines
the package version format. The root DUNE repository is named `root` in the base
recipe `SRC_URI`, and the user repository we're adding is named `user`.

### Changes to the DUNE core or build system

Changes other than custom Tasks and configurations require that you patch the
DUNE source tree. You can do this as usual by adding the patches to `SRC_URI`.
