package kairo.ktor

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.OutputStreamContent
import io.ktor.http.withCharsetIfNeeded
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.jvm.javaio.toInputStream
import kairo.reflect.KairoType
import kairo.serialization.KairoJson
import kairo.serialization.jsonGenerator
import kairo.serialization.reader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Adapted from [JacksonConverter], but avoids type erasure.
 */
@Suppress("MissingUseCall")
@OptIn(KairoJson.RawJsonMapper::class)
public class KairoConverter(private val json: KairoJson) : ContentConverter {
  override suspend fun serialize(
    contentType: ContentType,
    charset: Charset,
    typeInfo: TypeInfo,
    value: Any?,
  ): OutgoingContent =
    OutputStreamContent(
      {
        if (typeInfo.type == Flow::class) throw NotImplementedError()
        val type = type(typeInfo)
        val jsonGenerator = json.delegate.factory.createGenerator(writer(charset))
        json.jsonGenerator(jsonGenerator, value, type)
      },
      contentType.withCharsetIfNeeded(charset),
    )

  override suspend fun deserialize(
    charset: Charset,
    typeInfo: TypeInfo,
    content: ByteReadChannel,
  ): Any? {
    try {
      return withContext(Dispatchers.IO) {
        val type = type(typeInfo)
        val reader = content.toInputStream().reader(charset)
        json.reader(reader, type)
      }
    } catch (e: JsonParseException) {
      throw wrapException(e)
    } catch (e: JsonMappingException) {
      throw wrapException(e)
    }
  }

  private fun type(typeInfo: TypeInfo): KairoType<Any?> {
    val kotlinType = checkNotNull(typeInfo.kotlinType) { "Refusing to serialize without proper type information." }
    return KairoType(kotlinType)
  }

  private fun wrapException(e: JsonProcessingException): JsonConvertException =
    JsonConvertException("Illegal json parameter found: ${e.message}", e)
}

public fun io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig.kairoConversion(
  json: KairoJson = KairoJson(),
  contentType: ContentType = ContentType.Application.Json,
) {
  register(contentType, KairoConverter(json))
}

public fun io.ktor.server.plugins.contentnegotiation.ContentNegotiationConfig.kairoConversion(
  json: KairoJson = KairoJson(),
  contentType: ContentType = ContentType.Application.Json,
) {
  register(contentType, KairoConverter(json))
}
