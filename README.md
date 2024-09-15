# Kotools Samples

[![Kotools Types][kotools-samples-badge]][kotools-samples-project]
[![Embedded Kotlin][kotlin-embedded-badge]][kotlin]
[![Kotlin language][kotlin-language-badge]][kotlin]

**Kotools Samples** is a Gradle plugin designed to help Kotlin library authors
integrate **read-only, non-editable samples** into their documentation using
Dokka.
It addresses a limitation in Dokka, which does not allow making code samples
**non-editable** or **non-executable**.

This plugin also ensures that code samples are **inlined directly in the
documentation** and are **visible from the IDE**, enhancing the developer
experience.
Unlike many Kotlin libraries, such as Jetpack Compose, Kotools Samples ensures
your examples are not only available online but also accessible directly in the
IDE during development.

Currently, **Kotools Samples is not available yet**, but will **support the
Kotlin/JVM platform** starting from its first release.
**Kotlin Multiplatform support is planned for future releases**, allowing for
even more flexibility and broader application of the plugin.

[kotlin]: https://kotlinlang.org
[kotlin-embedded-badge]: https://img.shields.io/badge/Embedded_Kotlin-1.8.22-blue?logo=kotlin
[kotlin-language-badge]: https://img.shields.io/badge/Kotlin_language-1.5-blue?logo=kotlin
[kotools-samples-badge]: https://img.shields.io/maven-central/v/org.kotools/samples?label=Latest
[kotools-samples-project]: https://github.com/kotools/samples

## ⭐️ Key Features

- **Readonly inlined code samples:** Displays your examples inlined in the
  documentation, ensuring they are non-editable and non-executable, preventing
  unwanted modification or execution.
- **Kotlin or Java support:** Write your code samples in either Kotlin or Java,
  providing flexibility to serve a broader range of developers.
- **Sample visibility in IDE:** Unlike many libraries (such as Jetpack Compose),
  Kotools Samples makes your examples visible within the IDE (e.g.,
  IntelliJ IDEA, Android Studio), enhancing accessibility and usability during
  development.
- **Always correct samples:** Ensures your examples are always up-to-date by
  compiling them alongside your main and test sources. Any breaking changes in
  your codebase will trigger compilation errors, prompting you to update the
  examples.
- **Kotlin/JVM support:** Fully supports the Kotlin/JVM platform, with Kotlin
  Multiplatform support in future releases.
- **Seamless integration:** Works effortlessly with Kotlin, Java, and Dokka,
  smoothly integrating into your Gradle build process.

## 🧑‍💻 Usage

Here's a Kotlin sample:

```kotlin
// src/sample/kotlin/HelloKotlinSample.kt

import kotlin.test.Test
import kotlin.test.assertEquals

class HelloKotlinSample {
    @Test
    fun greet() {
        val name = "Sample"
        val greeting: String = greet(name)
        val expected = "Hello $name!"
        assertEquals(expected, greeting)
    }
}
```

And a Java sample:

```java
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloJavaSample {
    @Test
    void greet() {
        final String name = "Sample";
        final String greeting = HelloKt.greet(name);
        final String expected = "Hello %s!".formatted(name);
        Assertions.assertEquals(expected, greeting);
    }
}
```

Reference these samples in your Dokka documentation:

```kotlin
/**
 * Greets the user with the specified [name].
 *
 * Here's an example of calling this function from Kotlin code:
 *
 * SAMPLE: [HelloKotlinSample.greet]
 *
 * Here's an example of calling this function from Java code:
 *
 * SAMPLE: [HelloJavaSample.greet]
 */
fun greet(name: String = "World"): String = "Hello $name!"
```

When generating the documentation using Dokka, Kotools Samples will inline both
Kotlin and Java examples into the documentation, making them visible online and
in the IDE.

## 🚧 Contributing

Contributions are welcome! Feel free to submit bug reports, feature requests, or
pull requests to improve the plugin.

## 🙏 Acknowledgements

Thanks to [Loïc Lamarque](https://github.com/LVMVRQUXL) for creating and sharing
this project with the open source community.

Thanks to all the [people that ever contributed](https://github.com/kotools/samples/graphs/contributors)
through code or other means such as bug reports, feature suggestions and so on.

## 📄 License

This project is licensed under the [MIT License](LICENSE.txt).

---

With **Kotools Samples**, you can provide **inlined, read-only code samples** in
**Kotlin or Java**, ensuring your documentation remains accurate, visible, and
reliable both online and in the IDE.
Stay tuned for the **first release of Kotools Samples with Kotlin/JVM support**,
and **Kotlin Multiplatform support** in future releases!
