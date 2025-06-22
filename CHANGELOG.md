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

- Upgrade embedded Kotlin from 1.8.22 to 2.0.21, Dokka from 1.8.20 to 2.0.0, and
  support Dokka Gradle plugin v2 alongside its v1 ([#15] and [#41]).
- The `checkSampleSources` Gradle task now has improved error messages and looks
  for public classes in sample source files ([e61f6da6] and [c8450e5b]). This
  task now also fails if a sample source file contains a public nested class in
  a public class ([8a3eab89]). For Kotlin files, this task also checks that
  sample sources don't contain single-expression functions ([d5c73ada]).
  Finally, this task now logs all errors before failing the build ([605a1f5]).

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
[#41]: https://github.com/kotools/samples/issues/41
[2d249d2b]: https://github.com/kotools/samples/commit/2d249d2b
[359c3522]: https://github.com/kotools/samples/commit/359c3522
[605a1f5]: https://github.com/kotools/samples/commit/605a1f5
[8a3eab89]: https://github.com/kotools/samples/commit/8a3eab89
[beda12d9]: https://github.com/kotools/samples/commit/beda12d9
[c8450e5b]: https://github.com/kotools/samples/commit/c8450e5b
[d5c73ada]: https://github.com/kotools/samples/commit/d5c73ada
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
