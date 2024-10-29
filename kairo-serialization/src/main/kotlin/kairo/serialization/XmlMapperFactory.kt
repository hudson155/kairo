package kairo.serialization

import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter
import kairo.serialization.module.primitives.XmlPrimitivesModule

public class XmlMapperFactory internal constructor() :
  ObjectMapperFactory<XmlMapper, XmlMapper.Builder>() {
  override fun createBuilder(): XmlMapper.Builder {
    val factory = XmlFactory()
      .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
      .configure(ToXmlGenerator.Feature.WRITE_NULLS_AS_XSI_NIL, true)
      .configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true)
      .configure(ToXmlGenerator.Feature.WRITE_XML_SCHEMA_CONFORMING_FLOATS, true)
    return XmlMapper.Builder(XmlMapper(factory))
  }

  override fun configurePrimitives(builder: XmlMapper.Builder) {
    builder.addModule(XmlPrimitivesModule.create(this))
  }

  override fun configurePrettyPrinting(builder: XmlMapper.Builder) {
    super.configurePrettyPrinting(builder)
    builder.defaultPrettyPrinter(DefaultXmlPrettyPrinter())
  }
}

public fun xmlMapper(configure: XmlMapperFactory.() -> Unit = {}): XmlMapperFactory =
  XmlMapperFactory().apply(configure)
