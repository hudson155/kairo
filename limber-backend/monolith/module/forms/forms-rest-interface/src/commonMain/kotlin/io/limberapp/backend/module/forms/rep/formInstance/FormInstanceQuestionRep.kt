package io.limberapp.backend.module.forms.rep.formInstance

import io.limberapp.common.rep.CompleteRep
import io.limberapp.common.rep.CreationRep
import io.limberapp.common.types.LocalDateTime
import io.limberapp.common.types.UUID

object FormInstanceQuestionRep {
  interface Creation : CreationRep

  interface Complete : CompleteRep {
    override val createdDate: LocalDateTime
    val questionGuid: UUID?
  }
}
