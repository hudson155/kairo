package limber.serialization

internal class ObjectMapperFactoryYamlSerializationTest : ObjectMapperFactorySerializationTest(
  dataFormat = ObjectMapperFactory.Format.Yaml,
) {
  override val simpleData = """
    string: val
    int: 42
    boolean: true
  """.trimIndent() + '\n'
  override val simpleDataNull = """
    string: null
    int: null
    boolean: null
  """.trimIndent() + '\n'

  override val datesData = """
    zonedDateTime: 2007-12-03T05:15:30.789-07:00
    localDate: 2007-12-03
  """.trimIndent() + '\n'
  override val datesDataNull = """
    zonedDateTime: null
    localDate: null
  """.trimIndent() + '\n'

  override val enumData = """
    enum: Option1
  """.trimIndent() + '\n'
  override val enumDataNull = """
    enum: null
  """.trimIndent() + '\n'

  override val guidData = """
    guid: 8af24989-ba79-43fc-92cf-429110a22c80
  """.trimIndent() + '\n'
  override val guidDataNull = """
    guid: null
  """.trimIndent() + '\n'

  override val listDataEmpty = """
    list: []
  """.trimIndent() + '\n'
  override val listDataLength1 = """
    list:
      - 1
  """.trimIndent() + '\n'
  override val listDataLength2 = """
    list:
      - 1
      - 2
  """.trimIndent() + '\n'
  override val listDataLength1WithNull = """
    list:
      - null
  """.trimIndent() + '\n'
  override val listDataLength2WithNulls = """
    list:
      - null
      - null
  """.trimIndent() + '\n'
  override val listDataNull = """
    list: null
  """.trimIndent() + '\n'

  override val mapDataEmpty = """
    map: {}
  """.trimIndent() + '\n'
  override val mapDataLength1 = """
    map:
      1: 2
  """.trimIndent() + '\n'
  override val mapDataLength2 = """
    map:
      1: 2
      3: 4
  """.trimIndent() + '\n'
  override val mapDataLength1WithNullValue = """
    map:
      1: null
  """.trimIndent() + '\n'
  override val mapDataLength2WithNullValues = """
    map:
      1: null
      3: null
  """.trimIndent() + '\n'
  override val mapDataNull = """
    map: null
  """.trimIndent() + '\n'

  override val singletonData = """
    int: 42
    nullInt: null
  """.trimIndent() + '\n'

  override val unitRoot = "{}\n"
  override val unitData = """
    unit: {}
  """.trimIndent() + '\n'
  override val unitDataNull = """
    unit: null
  """.trimIndent() + '\n'
}
