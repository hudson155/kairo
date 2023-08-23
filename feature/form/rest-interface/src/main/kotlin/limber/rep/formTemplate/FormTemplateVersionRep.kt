package limber.rep.formTemplate

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import limber.rep.formTemplate.question.FormTemplateQuestionRep
import java.time.Instant

public data class FormTemplateVersionRep(
  val id: String,
  val templateId: String,
  val number: Int,
  val status: Status,
  val name: String,
  val questions: List<FormTemplateQuestionRep>,
) {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = Status.Draft::class, name = "Draft"),
    JsonSubTypes.Type(value = Status.Live::class, name = "Live"),
    JsonSubTypes.Type(value = Status.Archived::class, name = "Archived"),
  )
  public sealed class Status {
    public data object Draft : Status()

    public data class Live(
      val liveAt: Instant,
    ) : Status()

    public data class Archived(
      val liveAt: Instant?,
      val archivedAt: Instant,
    )
  }
}
