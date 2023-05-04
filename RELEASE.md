# Steps to release a new version

1. Decide whether to do a patch, minor or major release
2. Adjust the version in the [pom.xml](pom.xml)
3. Adjust the version in the [README.md](README.md)
4. Commit the changes _(the commit message will be the release notes, so make sure to include anything important; markdown is supported)_
5. Create a tag with the pom-version: `git tag 1.2.3`
6. Push your commit together with the tag
