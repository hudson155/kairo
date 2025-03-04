package kairo.rest.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.withCharsetIfNeeded
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.reifiedType
import io.ktor.utils.io.ByteReadChannel
import java.nio.charset.Charset

/**
 * The default Jackson converter does not take [TypeInfo] into account during serialization.
 * This is necessary in order to preserve type information when serializing lists of polymorphic classes.
 */
public class JacksonConverter(
  private val mapper: JsonMapper,
) : ContentConverter {
  private val delegate: JacksonConverter = JacksonConverter(mapper, streamRequestBody = false)

  override suspend fun serialize(
    contentType: ContentType,
    charset: Charset,
    typeInfo: TypeInfo,
    value: Any?,
  ): OutgoingContent {
    val type = mapper.constructType(typeInfo.reifiedType)
    val text = mapper.writerFor(type).writeValueAsString(value)
    return TextContent(
      text = text,
      contentType = contentType.withCharsetIfNeeded(charset),
    )
  }

  /**
   * For deserialization, we just delegate to the default Jackson converter.
   */
  override suspend fun deserialize(
    charset: Charset,
    typeInfo: TypeInfo,
    content: ByteReadChannel,
  ): Any? =
    delegate.deserialize(charset, typeInfo, content)
}
