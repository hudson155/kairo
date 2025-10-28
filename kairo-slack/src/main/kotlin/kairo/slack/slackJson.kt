package kairo.slack

import kairo.serialization.json
import kotlinx.serialization.json.Json

public val slackJson: Json = json(prettyPrint = true)
