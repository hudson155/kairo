package limber.serialization

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import limber.time.inUtc
import org.junit.jupiter.api.Test
import java.util.UUID

@Suppress("ClassOrdering") // In this case it's nice to keep properties near their test methods.
internal abstract class ObjectMapperFactoryDeserializationTest(
  dataFormat: ObjectMapperFactory.Format,
) : ObjectMapperFactoryTest(dataFormat) {
  protected data class InvalidData(val int: Int)

  protected abstract val invalidDataMissing: String
  protected abstract val invalidDataNull: String
  protected abstract val invalidDataWrongType: String
  protected abstract val invalidDataExtraProperty: String

  @Test
  fun invalid() {
    shouldThrow<JacksonException> {
      objectMapper.readValue<InvalidData>(invalidDataMissing)
    }
    shouldThrow<JacksonException> {
      objectMapper.readValue<InvalidData>(invalidDataNull)
    }
    shouldThrow<JacksonException> {
      objectMapper.readValue<InvalidData>(invalidDataWrongType)
    }
    shouldThrow<JacksonException> {
      objectMapper.readValue<InvalidData>(invalidDataExtraProperty)
    }
  }

  protected abstract val simpleData: String
  protected abstract val simpleDataMissing: String

  @Test
  final override fun simple() {
    objectMapper.readValue<SimpleData>(simpleData)
      .shouldBe(SimpleData("val", 42, true))
    objectMapper.readValue<SimpleData>(simpleDataMissing)
      .shouldBe(SimpleData(null, null, null))
  }

  protected abstract val datesData: String
  protected abstract val datesDataMissing: String

  @Test
  final override fun dates() {
    objectMapper.readValue<DatesData>(datesData)
      .shouldBe(DatesData(zonedDateTime.inUtc(), localDate))
    objectMapper.readValue<DatesData>(datesDataMissing)
      .shouldBe(DatesData(null, null))
  }

  protected abstract val enumData: String
  protected abstract val enumDataMissing: String
  protected abstract val enumDataUnrecognized: String

  @Test
  final override fun enum() {
    objectMapper.readValue<EnumData>(enumData)
      .shouldBe(EnumData(EnumData.Enum.Option1))
    objectMapper.readValue<EnumData>(enumDataMissing)
      .shouldBe(EnumData(null))
    shouldThrow<JacksonException> {
      objectMapper.readValue<EnumData>(enumDataUnrecognized)
    }
  }

  protected abstract val guidData: String
  protected abstract val guidDataMissing: String
  protected abstract val guidDataMalformed: String

  @Test
  final override fun guid() {
    objectMapper.readValue<GuidData>(guidData)
      .shouldBe(GuidData(UUID.fromString("8af24989-ba79-43fc-92cf-429110a22c80")))
    objectMapper.readValue<GuidData>(guidDataMissing)
      .shouldBe(GuidData(null))
    shouldThrow<JacksonException> {
      objectMapper.readValue<EnumData>(guidDataMalformed)
    }
  }

  protected abstract val listDataEmpty: String
  protected abstract val listDataLength1: List<String>
  protected abstract val listDataLength2: List<String>
  protected abstract val listDataLength1WithNull: List<String>
  protected abstract val listDataLength2WithNulls: List<String>
  protected abstract val listDataMissing: String

  @Test
  final override fun list() {
    objectMapper.readValue<ListData>(listDataEmpty)
      .shouldBe(ListData(emptyList()))
    listDataLength1.forEach { data ->
      objectMapper.readValue<ListData>(data)
        .shouldBe(ListData(listOf(1)))
    }
    listDataLength2.forEach { data ->
      objectMapper.readValue<ListData>(data)
        .shouldBe(ListData(listOf(1, 2)))
    }
    listDataLength1WithNull.forEach { data ->
      objectMapper.readValue<ListData>(data)
        .shouldBe(ListData(listOf(null)))
    }
    listDataLength2WithNulls.forEach { data ->
      objectMapper.readValue<ListData>(data)
        .shouldBe(ListData(listOf(null, null)))
    }
    objectMapper.readValue<ListData>(listDataMissing)
      .shouldBe(ListData(null))
  }

  protected abstract val mapDataEmpty: String
  protected abstract val mapDataLength1: String
  protected abstract val mapDataLength2: String
  protected abstract val mapDataLength1WithNullKey: String
  protected abstract val mapDataLength1WithNullValue: String
  protected abstract val mapDataLength2WithNullValues: String
  protected abstract val mapDataMissing: String

  @Test
  final override fun map() {
    objectMapper.readValue<MapData>(mapDataEmpty)
      .shouldBe(MapData(emptyMap()))
    objectMapper.readValue<MapData>(mapDataLength1)
      .shouldBe(MapData(mapOf(1 to 2)))
    objectMapper.readValue<MapData>(mapDataLength2)
      .shouldBe(MapData(mapOf(1 to 2, 3 to 4)))
    shouldThrow<JacksonException> {
      objectMapper.readValue<MapData>(mapDataLength1WithNullKey)
    }
    objectMapper.readValue<MapData>(mapDataLength1WithNullValue)
      .shouldBe(MapData(mapOf(1 to null)))
    objectMapper.readValue<MapData>(mapDataLength2WithNullValues)
      .shouldBe(MapData(mapOf(1 to null, 3 to null)))
    objectMapper.readValue<MapData>(mapDataMissing)
      .shouldBe(MapData(null))
  }

  protected abstract val singletonData: String

  @Test
  final override fun singleton() {
    objectMapper.readValue<SingletonData>(singletonData)
      .shouldBe(SingletonData)
  }

  protected abstract val unitRoot: String
  protected abstract val unitData: String
  protected abstract val unitDataMissing: String

  @Test
  final override fun unit() {
    objectMapper.readValue<Unit>(unitRoot)
      .shouldBe(Unit)
    objectMapper.readValue<UnitData>(unitData)
      .shouldBe(UnitData(Unit))
    objectMapper.readValue<UnitData>(unitDataMissing)
      .shouldBe(UnitData(null))
  }
}
