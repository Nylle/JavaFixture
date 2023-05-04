# Steps to release a new version

1. Decide whether to do a patch, minor or major release
2. Adjust the version in the [pom.xml](https://github.com/Nylle/JavaFixture/blob/master/pom.xml#L10)
3. Adjust the version in the [README.md](https://github.com/Nylle/JavaFixture/blob/master/README.md?plain=1#L25)
4. Commit the changes _(the commit message will be the release notes, so make sure to include anything important; markdown is supported)_
5. Create a tag with the pom-version: `git tag 1.2.3`
6. Push your commit together with the tag
