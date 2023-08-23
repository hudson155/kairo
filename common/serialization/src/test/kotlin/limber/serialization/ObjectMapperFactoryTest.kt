package limber.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

internal abstract class ObjectMapperFactoryTest(dataFormat: ObjectMapperFactory.Format) {
  protected val localDate: LocalDate = LocalDate.of(2007, 12, 3)
  protected val zonedDateTime: ZonedDateTime = run {
    val localTime = LocalTime.of(5, 15, 30, 789_000_000)
    return@run ZonedDateTime.of(localDate, localTime, ZoneId.of("America/Edmonton"))
  }

  protected val objectMapper: ObjectMapper = ObjectMapperFactory.builder(dataFormat).build()

  protected data class SimpleData(val string: String?, val int: Int?, val boolean: Boolean?)

  abstract fun simple()

  protected data class DatesData(val zonedDateTime: ZonedDateTime?, val localDate: LocalDate?)

  abstract fun dates()

  protected data class EnumData(val enum: Enum?) {
    enum class Enum {
      Option1,
      Option2,
      ;

      override fun toString(): String = "unused"
    }
  }

  abstract fun enum()

  protected data class GuidData(val guid: UUID?)

  abstract fun guid()

  protected data class ListData(val list: List<Int?>?)

  abstract fun list()

  protected data class MapData(val map: Map<Int?, Int?>?)

  abstract fun map()

  protected data object SingletonData {
    const val constInt: Int = 42

    @Suppress("MayBeConst")
    val int: Int = 42

    val nullInt: Int? = null
  }

  abstract fun singleton()

  data class UnitData(val unit: Unit?)

  abstract fun unit()
}
