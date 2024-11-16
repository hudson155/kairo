# `kairo-slack-feature`

Kairo's Slack Feature provides a basic interface to send Slack messages.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  implementation("kairo:kairo-slack-feature:$kairoVersion")
}
```

### Step 2: Create and configure the Feature

```yaml
# src/main/resources/config/config.yaml

slack:
  type: "Real"
  token: "xoxb-your-slack-bot-token"
  channels:
    my-channel: "C0802ABC1Z3"
```

```kotlin
// src/main/kotlin/yourPackage/server/monolith/MonolithServer.kt

KairoSlackFeature(config.slack)
```

### Step 3: Inject and use the Slack client

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class MyClass @Inject constructor(
  private val slackClient: SlackClient
) {
  fun myMethod() {
    slackClient.sendMessage(
      channelName = "my-channel",
      message = withBlocks {
        header { text("Message from Kairo") }
        section {
          markdownText("This is a message from Kairo!")
        }
      },
    )
  }
}
```
