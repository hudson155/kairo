package kairo.sql.plugin.jackson

import org.jdbi.v3.jackson2.Jackson2Config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.result.UnableToProduceResultException
import org.jdbi.v3.json.JsonMapper
import java.io.IOException
import java.lang.reflect.Type
import kotlin.jvm.java

/**
 * Forked from JDBI's JacksonJsonMapper.
 *
 * This is identical to the JDBI version, except that it uses a separate read and write mappers
 * via KairoJacksonJdbiConfig.
 *
 * JDBI license note:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
internal class KairoJacksonJsonMapper : JsonMapper {
  override fun forType(type: Type, config: ConfigRegistry): JsonMapper.TypedJsonMapper {
    return object : JsonMapper.TypedJsonMapper {
      private val writeMapper: ObjectMapper = config.get(KairoJacksonJdbiConfig::class.java).writeMapper
      private val readMapper: ObjectMapper = config.get(KairoJacksonJdbiConfig::class.java).readMapper
      private val writeMappedType: JavaType = writeMapper.constructType(type)
      private val readMappedType: JavaType = readMapper.constructType(type)
      private val reader: ObjectReader = readMapper.readerFor(readMappedType)
      private val writer: ObjectWriter = writeMapper.writerFor(writeMappedType)

      override fun toJson(value: Any?, config: ConfigRegistry): String? {
        val view: Class<*>? = config.get(Jackson2Config::class.java).serializationView
        val viewWriter: ObjectWriter =
          if (view == null)
            writer
          else
            writer.withView(view)
        try {
          return viewWriter.writeValueAsString(value)
        } catch (e: JsonProcessingException) {
          throw UnableToProduceResultException(e)
        }
      }

      override fun fromJson(json: String, config: ConfigRegistry): Any? {
        val view: Class<*>? = config.get(Jackson2Config::class.java).deserializationView
        val viewReader: ObjectReader =
          if (view == null)
            reader
          else
            reader.withView(view)
        try {
          return viewReader.readValue(json)
        } catch (e: IOException) {
          throw UnableToProduceResultException(e)
        }
      }
    }
  }
}
