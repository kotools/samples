# Kotools Samples for Kotlin/JVM - Demo

This project showcases how to use the Kotools Samples Gradle plugin for
Kotlin/JVM projects.

## 🧪 Checks

For checking this demo project, run the following command in your terminal from
the root directory `samples`:

```shell
./gradlew :jvm-demo:check
```

This command will execute Kotlin and Java tests present in the source sets
`sample` and `test`.

## 📝 Documentation

For generating the API reference of this project using Dokka, run the following
command in your terminal from the root directory `samples`:

```shell
./gradlew :jvm-demo:dokkaHtml
```

In your IDE, right-click on the `build/dokka/html/index.html` file, then choose
your favorite browser in the `Open In > Browser` menu. This will serve the
documentation locally.

![Screenshot](screenshot.png)
