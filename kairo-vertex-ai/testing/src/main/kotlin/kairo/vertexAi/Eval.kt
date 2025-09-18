package kairo.vertexAi

public fun evalPrompt(userMessage: String, modelResponse: String): String =
  """
    <Root>
      <UserMessage>$userMessage</UserMessage>
      <ModelResponse>$modelResponse</ModelResponse>
    </Root>
  """.trimIndent()
