package kairo.vertexAi.schema

import kotlinx.serialization.SerialInfo

/**
 * A collection of namespaced annotations for Vertex AI schema generation.
 */
public object Vertex {
  @Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
  @Retention(AnnotationRetention.SOURCE)
  @SerialInfo
  public annotation class Description(val value: String)

  @Target(AnnotationTarget.PROPERTY)
  @Retention(AnnotationRetention.SOURCE)
  @SerialInfo
  public annotation class Format(val value: String)
}
