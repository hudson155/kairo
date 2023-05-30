package limber.rep.formTemplate.question

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = BooleanQuestionRep::class, name = "Boolean"),
  JsonSubTypes.Type(value = DateTimeQuestionRep::class, name = "DateTime"),
  JsonSubTypes.Type(value = TextQuestionRep::class, name = "Text"),
)
public sealed class FormTemplateQuestionRep {
  public abstract val id: String
  public abstract val required: Required
  public abstract val label: String

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = Required.Always::class, name = "Always"),
    JsonSubTypes.Type(value = Required.BooleanQuestion::class, name = "BooleanQuestion"),
    JsonSubTypes.Type(value = Required.Never::class, name = "Never"),
  )
  public sealed class Required {
    public object Always : Required()

    public data class BooleanQuestion(
      val questionId: String,
      val condition: Boolean,
    ) : Required()

    public object Never : Required()
  }
}
