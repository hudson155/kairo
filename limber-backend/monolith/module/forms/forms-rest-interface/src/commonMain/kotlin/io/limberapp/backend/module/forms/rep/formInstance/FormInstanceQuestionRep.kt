package io.limberapp.backend.module.forms.rep.formInstance

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.types.LocalDateTime
import com.piperframework.types.UUID

object FormInstanceQuestionRep {
  interface Creation : CreationRep

  interface Complete : CompleteRep {
    override val createdDate: LocalDateTime
    val questionGuid: UUID?
  }
}
