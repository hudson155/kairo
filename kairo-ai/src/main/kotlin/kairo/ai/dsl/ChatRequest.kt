package kairo.ai.dsl

import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.model.chat.request.ChatRequest

public fun ChatRequest.Builder.messages(
  block: MutableList<ChatMessage>.() -> Unit,
): ChatRequest.Builder =
  messages(buildList(block))
