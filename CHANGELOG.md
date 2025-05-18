# 🔄 Changelog

All notable changes to this project will be documented in this file.

> The format of this document is based on
> [Keep a Changelog](https://keepachangelog.com/en/1.1.0).

## 🤔 Types of changes

- `✨ Added` for new features.
- `♻️ Changed` for changes in existing functionality.
- `🗑️ Deprecated` for soon-to-be removed features.
- `🔥 Removed` for now removed features.
- `🐛 Fixed` for any bug fixes.
- `🔒 Security` in case of vulnerabilities.

## 🚧 Unreleased

### ✨ Added

- The `cleanMainSourcesBackup` Gradle task for making sure that main sources are
  always backed-up ([2d249d2b]).

### ♻️ Changed

- Upgrade embedded Kotlin from 1.8.22 to 1.9.25, and Dokka from 1.8.20 to 1.9.20
  ([#15]). 
- Improve error messages of the `checkSampleSources` Gradle task ([e61f6da6]).

### 🔥 Removed

- The `sample` Kotlin source set and the `checkSampleKotlinSourceSet` Gradle
  task ([#25]).
- The `createKotoolsSamplesBuildDirectory` and the `cleanSamplesBuildDirectory`
  Gradle tasks, after migrating the output directory to `kotools-samples`
  ([beda12d9] and [359c3522]).

---

Thanks to [@LVMVRQUXL] for contributing to this release. 🙏

[@LVMVRQUXL]: https://github.com/LVMVRQUXL
[#15]: https://github.com/kotools/samples/issues/15
[#25]: https://github.com/kotools/samples/issues/25
[2d249d2b]: https://github.com/kotools/samples/commit/2d249d2b
[359c3522]: https://github.com/kotools/samples/commit/359c3522
[beda12d9]: https://github.com/kotools/samples/commit/beda12d9
[e61f6da6]: https://github.com/kotools/samples/commit/e61f6da6

## 🔖 Releases

| Version | Release date |
|---------|--------------|
| [0.3.0] | 2025-03-09   |
| [0.2.0] | 2025-02-03   |
| [0.1.0] | 2024-11-17   |

[0.3.0]: https://github.com/kotools/samples/releases/tag/0.3.0
[0.2.0]: https://github.com/kotools/samples/releases/tag/0.2.0
[0.1.0]: https://github.com/kotools/samples/releases/tag/0.1.0
