package limber.serialization

internal class ObjectMapperFactoryJsonSerializationTest : ObjectMapperFactorySerializationTest(
  dataFormat = ObjectMapperFactory.Format.Json,
) {
  override val simpleData = "{\"string\":\"val\",\"int\":42,\"boolean\":true}"
  override val simpleDataNull = "{\"string\":null,\"int\":null,\"boolean\":null}"

  override val datesData = "{\"zonedDateTime\":\"2007-12-03T05:15:30.789-07:00\",\"localDate\":\"2007-12-03\"}"
  override val datesDataNull = "{\"zonedDateTime\":null,\"localDate\":null}"

  override val enumData = "{\"enum\":\"Option1\"}"
  override val enumDataNull = "{\"enum\":null}"

  override val guidData = "{\"guid\":\"8af24989-ba79-43fc-92cf-429110a22c80\"}"
  override val guidDataNull = "{\"guid\":null}"

  override val listDataEmpty = "{\"list\":[]}"
  override val listDataLength1 = "{\"list\":[1]}"
  override val listDataLength2 = "{\"list\":[1,2]}"
  override val listDataLength1WithNull = "{\"list\":[null]}"
  override val listDataLength2WithNulls = "{\"list\":[null,null]}"
  override val listDataNull = "{\"list\":null}"

  override val mapDataEmpty = "{\"map\":{}}"
  override val mapDataLength1 = "{\"map\":{\"1\":2}}"
  override val mapDataLength2 = "{\"map\":{\"1\":2,\"3\":4}}"
  override val mapDataLength1WithNullValue = "{\"map\":{\"1\":null}}"
  override val mapDataLength2WithNullValues = "{\"map\":{\"1\":null,\"3\":null}}"
  override val mapDataNull = "{\"map\":null}"

  override val singletonData = "{\"int\":42,\"nullInt\":null}"

  override val unitRoot = "{}"
  override val unitData = "{\"unit\":{}}"
  override val unitDataNull = "{\"unit\":null}"
}
