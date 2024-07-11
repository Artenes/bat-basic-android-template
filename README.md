# BAT (Basic Android Template)

![Logo](images/logo.png)

([Logo by ghost_icon](https://www.freepik.com/icon/bat_3717068))

# Features

- single module project
- ViewModel
- Hilt
- Compose
- Timber
- Material Icons (extended)
- JDK Desugar
- Java 17

# .env

There is a file in the root called `secrets.properties.example`.

You copy it and rename it to `secrets.properties`.

In there you can put any secret value for your project and they will be accessible in
your `build.gradle`.

If you provide the values present in the example, a `release` signing configuration will be
automatically created in your `build.gradle` file.

# How to use it

To start a new project:

- Update project name in ``settings.gradle``
- Update package structure at folder ``app/src/main/java``
- Then update package name at ``app/build.gradle``
- Whenever you need to build the release flavor, make a copy of `secrets.properties.example` and
  rename it to `secrets.properties`. Fill in the values for your keystore and the release flavor
  will be signed using these information.