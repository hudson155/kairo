package limber.rep.formTemplate.question

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import limber.validation.FormTemplateQuestionLabelValidator
import java.time.ZonedDateTime
import java.util.UUID

public data class DateTimeQuestionRep(
  override val guid: UUID,
  override val required: Required,
  @FormTemplateQuestionLabelValidator override val label: String,
  val min: Option?,
  val max: Option?,
  val default: Option?,
) : FormTemplateQuestionRep() {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = Option.Explicit::class, name = "Explicit"),
    JsonSubTypes.Type(value = Option.Now::class, name = "Now"),
  )
  public sealed class Option {
    public data class Explicit(
      val value: ZonedDateTime,
    ) : Option()

    public object Now : Option()
  }
}
