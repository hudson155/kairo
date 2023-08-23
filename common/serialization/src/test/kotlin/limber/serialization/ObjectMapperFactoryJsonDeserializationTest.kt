package limber.serialization

internal class ObjectMapperFactoryJsonDeserializationTest : ObjectMapperFactoryDeserializationTest(
  dataFormat = ObjectMapperFactory.Format.Json,
) {
  override val invalidDataMissing = "{}\n"
  override val invalidDataNull =
    """
    {
      "int": null
    }
    """.trimIndent() + '\n'
  override val invalidDataWrongType =
    """
    {
      "int": "val"
    }
    """.trimIndent() + '\n'
  override val invalidDataExtraProperty =
    """
    {
      "int": 42,
      "string": "val"
    }
    """.trimIndent() + '\n'

  override val simpleData =
    """
    {
      "string": "val",
      "int": 42,
      "boolean": true
    }
    """.trimIndent() + '\n'
  override val simpleDataMissing = "{}\n"

  override val datesData =
    """
    {
      "zonedDateTime": "2007-12-03T05:15:30.789-07:00",
      "localDate": "2007-12-03"
    }
    """.trimIndent() + '\n'
  override val datesDataMissing = "{}\n"

  override val enumData =
    """
    {
      "enum": "Option1"
    }
    """.trimIndent() + '\n'
  override val enumDataMissing = "{}\n"
  override val enumDataUnrecognized =
    """
    {
      "enum": "Unrecognized"
    }
    """.trimIndent() + '\n'

  override val guidData =
    """
    {
      "guid": "8af24989-ba79-43fc-92cf-429110a22c80"
    }
    """.trimIndent() + '\n'
  override val guidDataMissing = "{}\n"
  override val guidDataMalformed =
    """
    {
      "guid": "val"
    }
    """.trimIndent() + '\n'

  override val listDataEmpty =
    """
    {
      "list": []
    }
    """.trimIndent() + '\n'
  override val listDataLength1 = listOf(
    """
    {
      "list": [
        1
      ]
    }
    """.trimIndent() + '\n',
  )
  override val listDataLength2 = listOf(
    """
    {
      "list": [
        1,
        2
      ]
    }
    """.trimIndent() + '\n',
  )
  override val listDataLength1WithNull = listOf(
    """
    {
      "list": [
        null
      ]
    }
    """.trimIndent() + '\n',
  )
  override val listDataLength2WithNulls = listOf(
    """
    {
      "list": [
        null,
        null
      ]
    }
    """.trimIndent() + '\n',
  )
  override val listDataMissing = "{}\n"

  override val mapDataEmpty =
    """
    {
      "map": {}
    }
    """.trimIndent() + '\n'
  override val mapDataLength1 =
    """
    {
      "map": {
        "1": 2
      }
    }
    """.trimIndent() + '\n'
  override val mapDataLength2 =
    """
    {
      "map": {
        "1": 2,
        "3": 4
      }
    }
    """.trimIndent() + '\n'
  override val mapDataLength1WithNullKey =
    """
    {
      "map": {
        null: 2
      }
    }
    """.trimIndent() + '\n'
  override val mapDataLength1WithNullValue =
    """
    {
      "map": {
        "1": null
      }
    }
    """.trimIndent() + '\n'
  override val mapDataLength2WithNullValues =
    """
    {
      "map": {
        "1": null,
        "3": null
      }
    }
    """.trimIndent() + '\n'
  override val mapDataMissing = "{}\n"

  override val singletonData = "{}\n"

  override val unitRoot = "{}\n"
  override val unitData =
    """
    {
      "unit": {}
    }
    """.trimIndent() + '\n'
  override val unitDataMissing = "{}\n"
}
