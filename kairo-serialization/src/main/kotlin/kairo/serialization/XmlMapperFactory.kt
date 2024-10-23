package kairo.serialization

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter
import kairo.serialization.module.primitives.XmlPrimitivesModule

public class XmlMapperFactory internal constructor(
  modules: List<Module>,
) : ObjectMapperFactory<XmlMapper, XmlMapper.Builder>(modules) {
  override fun createBuilder(): XmlMapper.Builder {
    val factory = XmlFactory()
      .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
      .configure(ToXmlGenerator.Feature.WRITE_NULLS_AS_XSI_NIL, true)
      .configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true)
      .configure(ToXmlGenerator.Feature.WRITE_XML_SCHEMA_CONFORMING_FLOATS, true)
    return XmlMapper.Builder(XmlMapper(factory))
  }

  override fun configurePrimitives(builder: XmlMapper.Builder) {
    builder.addModule(XmlPrimitivesModule(trimWhitespace = trimWhitespace))
  }

  override fun configurePrettyPrinting(builder: XmlMapper.Builder) {
    super.configurePrettyPrinting(builder)

    builder.defaultPrettyPrinter(DefaultXmlPrettyPrinter())
  }
}

public fun xmlMapper(
  vararg modules: Module,
  block: XmlMapperFactory.() -> Unit = {},
): XmlMapper =
  XmlMapperFactory(modules.toList()).apply(block).build()
