package kairo.vertexAi.dsl

import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import java.net.URL

public fun generateContentConfig(
  block: GenerateContentConfig.Builder.() -> Unit = {},
): GenerateContentConfig =
  GenerateContentConfig.builder().apply(block).build()

public fun GenerateContentConfig.Builder.systemInstruction(url: URL) {
  systemInstruction(url.readText())
}

public fun GenerateContentConfig.Builder.systemInstruction(string: String) {
  val content = Content.fromParts(Part.fromText(string))
  systemInstruction(content)
}
