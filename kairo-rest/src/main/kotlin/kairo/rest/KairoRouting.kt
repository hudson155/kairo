package kairo.rest

import kairo.reflect.KairoType

@Suppress("UseDataClass")
public class KairoRouting<E : RestEndpoint<*, *>>(
  public val input: KairoType<*>,
  public val output: KairoType<*>,
  public val endpoint: KairoType<E>,
)
