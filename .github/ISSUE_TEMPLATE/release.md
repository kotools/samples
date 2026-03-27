---
name: 🔖 Release
about: Template for publishing a new version.
title: 🔖 Release version $VERSION
assignees: LVMVRQUXL
labels: release
---

## 🔗 Dependencies

Issues of the milestone corresponding to this version should be done before resolving this issue.

## ✅ Checklist

- [ ] 🔖 Set Gradle project's version to this new one.
- [ ] 📄 Check copyright notice in [license].
- [ ] 🚀 Run [delivery workflow] for publishing sources, and release distribution on [Maven Central].
- [ ] 🚀 Run [deployment workflow] for publishing Gradle plugin.
- [ ] 📝 Move unreleased changelog to GitHub release draft, and add new release in changelog.
- [ ] 🔖 Publish the GitHub release.
- [ ] 🔖 Set Gradle project's version to the next snapshot.
- [ ] 📝 Announce this release on [Reddit], [Slack] and [Twitter].

[delivery workflow]: https://github.com/kotools/samples/actions/workflows/delivery.yml
[deployment workflow]: https://github.com/kotools/samples/actions/workflows/deployment.yml
[license]: https://github.com/kotools/samples/blob/main/LICENSE.txt
[maven central]: https://central.sonatype.com
[reddit]: https://www.reddit.com/r/Kotlin
[slack]: https://kotlinlang.slack.com/archives/C05H0L1LD25
[twitter]: https://twitter.com/kotools_org
