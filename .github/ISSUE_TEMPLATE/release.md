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
- [ ] 📄 Check the copyright notice in the [license] documentation.
- [ ] 🚀 Deliver the plugin by running the [delivery workflow].
- [ ] 📝 Move the unreleased changelog to a GitHub release draft.
- [ ] 🔖 Publish the GitHub release.
- [ ] 🔖 Set Gradle project's version to the next snapshot.
- [ ] 📝 Announce this release on [Reddit], [Slack] and [Twitter].

[delivery workflow]: https://github.com/kotools/samples/actions/workflows/delivery.yml
[license]: https://github.com/kotools/samples/blob/main/LICENSE.txt
[maven central]: https://s01.oss.sonatype.org
[reddit]: https://www.reddit.com/r/Kotlin
[slack]: https://kotlinlang.slack.com/archives/C05H0L1LD25
[twitter]: https://twitter.com/kotools_org
