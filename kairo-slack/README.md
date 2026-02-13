# Slack

Leverages Slack's native [JVM client](https://github.com/slackapi/java-slack-sdk) and Kotlin DSL
to make Slack interaction easy.

Also offers channel mapping functionality
so you can refer to channels by name instead of ID.

## Installation

Install `kairo-slack-feature`.
You don't need to install the Slack client separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("com.highbeam.kairo:kairo-slack-feature")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val features = listOf(
  SlackFeature(config.slack),
)
```

We recommend using [kairo-config](../kairo-config/README.md) to configure the Feature.

```hocon
slack {
  token = ${SLACK_TOKEN}
  channels {
    "general" = "C00SPPX83RS"
  }
}
```

Now send a Slack message!

```kotlin
slack.chatPostMessage { builder ->
  builder.channel(slack.channels.getValue("general"))
  builder.blocks {
    section {
      plainText("Hello, World!")
    }
  }
}
```
