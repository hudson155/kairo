package limber.serialization

import com.fasterxml.jackson.core.JacksonException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

@Suppress("ClassOrdering") // In this case it's nice to keep properties near their test methods.
internal abstract class ObjectMapperFactorySerializationTest(
  dataFormat: ObjectMapperFactory.Format,
) : ObjectMapperFactoryTest(dataFormat) {
  protected abstract val simpleData: String
  protected abstract val simpleDataNull: String

  @Test
  final override fun simple() {
    objectMapper.writeValueAsString(SimpleData("val", 42, true))
      .shouldBe(simpleData)
    objectMapper.writeValueAsString(SimpleData(null, null, null))
      .shouldBe(simpleDataNull)
  }

  protected abstract val datesData: String
  protected abstract val datesDataNull: String

  @Test
  final override fun dates() {
    objectMapper.writeValueAsString(DatesData(zonedDateTime, localDate))
      .shouldBe(datesData)
    objectMapper.writeValueAsString(DatesData(null, null))
      .shouldBe(datesDataNull)
  }

  protected abstract val enumData: String
  protected abstract val enumDataNull: String

  @Test
  final override fun enum() {
    objectMapper.writeValueAsString(EnumData(EnumData.Enum.Option1))
      .shouldBe(enumData)
    objectMapper.writeValueAsString(EnumData(null))
      .shouldBe(enumDataNull)
  }

  protected abstract val guidData: String
  protected abstract val guidDataNull: String

  @Test
  final override fun guid() {
    objectMapper.writeValueAsString(GuidData(UUID.fromString("8af24989-ba79-43fc-92cf-429110a22c80")))
      .shouldBe(guidData)
    objectMapper.writeValueAsString(GuidData(null))
      .shouldBe(guidDataNull)
  }

  protected abstract val listDataEmpty: String
  protected abstract val listDataLength1: String
  protected abstract val listDataLength2: String
  protected abstract val listDataLength1WithNull: String
  protected abstract val listDataLength2WithNulls: String
  protected abstract val listDataNull: String

  @Test
  final override fun list() {
    objectMapper.writeValueAsString(ListData(emptyList()))
      .shouldBe(listDataEmpty)
    objectMapper.writeValueAsString(ListData(listOf(1)))
      .shouldBe(listDataLength1)
    objectMapper.writeValueAsString(ListData(listOf(1, 2)))
      .shouldBe(listDataLength2)
    objectMapper.writeValueAsString(ListData(listOf(null)))
      .shouldBe(listDataLength1WithNull)
    objectMapper.writeValueAsString(ListData(listOf(null, null)))
      .shouldBe(listDataLength2WithNulls)
    objectMapper.writeValueAsString(ListData(null))
      .shouldBe(listDataNull)
  }

  protected abstract val mapDataEmpty: String
  protected abstract val mapDataLength1: String
  protected abstract val mapDataLength2: String
  protected abstract val mapDataLength1WithNullValue: String
  protected abstract val mapDataLength2WithNullValues: String
  protected abstract val mapDataNull: String

  @Test
  final override fun map() {
    objectMapper.writeValueAsString(MapData(emptyMap()))
      .shouldBe(mapDataEmpty)
    objectMapper.writeValueAsString(MapData(mapOf(1 to 2)))
      .shouldBe(mapDataLength1)
    objectMapper.writeValueAsString(MapData(mapOf(1 to 2, 3 to 4)))
      .shouldBe(mapDataLength2)
    shouldThrow<JacksonException> { // Null key.
      objectMapper.writeValueAsString(MapData(mapOf(null to 2)))
    }
    objectMapper.writeValueAsString(MapData(mapOf(1 to null)))
      .shouldBe(mapDataLength1WithNullValue)
    objectMapper.writeValueAsString(MapData(mapOf(1 to null, 3 to null)))
      .shouldBe(mapDataLength2WithNullValues)
    objectMapper.writeValueAsString(MapData(null))
      .shouldBe(mapDataNull)
  }

  protected abstract val singletonData: String

  @Test
  final override fun singleton() {
    objectMapper.writeValueAsString(SingletonData)
      .shouldBe(singletonData)
  }

  protected abstract val unitRoot: String
  protected abstract val unitData: String
  protected abstract val unitDataNull: String

  @Test
  final override fun unit() {
    objectMapper.writeValueAsString(Unit)
      .shouldBe(unitRoot)
    objectMapper.writeValueAsString(UnitData(Unit))
      .shouldBe(unitData)
    objectMapper.writeValueAsString(UnitData(null))
      .shouldBe(unitDataNull)
  }
}
