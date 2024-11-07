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

- `org.kotools.samples.jvm` Gradle plugin for Kotlin/JVM projects, supporting
  Kotlin 1.8.22 and Dokka 1.8.20. The following bullet points refer to changes
  made to this Gradle plugin.
- `sample` Kotlin source set dedicated to code samples written in Kotlin or
  Java. By default, the `test` source set depends on the `sample` one, which
  depends on the `main` one. This configuration allows to execute code samples
  alongside unit tests, ensuring their correctness.
- `checkSampleSources` task for checking the content of sample sources. This
  task fails if a sample source doesn't have a single class.
- `extractSamples` task that extracts samples from sources as Markdown for KDoc.
- `checkSampleReferences` task for checking that KDoc sample references targets
  existing code samples.
- `backupMainSources` task for saving main sources before inlining samples.
- `inlineSamples` task that inlines samples in KDoc as Markdown code blocks.
- `restoreMainSources` task for restoring main sources after inlining samples.

---

Thanks to [@LVMVRQUXL] for contributing to this new release. 🙏

[@LVMVRQUXL]: https://github.com/LVMVRQUXL
