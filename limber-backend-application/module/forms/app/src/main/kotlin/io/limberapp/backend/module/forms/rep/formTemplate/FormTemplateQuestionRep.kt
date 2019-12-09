package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteSubrep
import com.piperframework.rep.CreationSubrep
import com.piperframework.rep.UpdateSubrep
import com.piperframework.validation.util.ifPresent
import com.piperframework.validation.util.longText
import com.piperframework.validation.util.mediumText
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

object FormTemplateQuestionRep {

    interface Creation : CreationSubrep {

        val label: String
        val helpText: String?
        val width: FormTemplateQuestionModel.Width

        override fun validate() {
            validate(Creation::label) { mediumText(allowEmpty = false) }
            validate(Creation::helpText) { ifPresent { longText(allowEmpty = false) } }
        }
    }

    interface Complete : CompleteSubrep {

        val id: UUID
        val label: String
        val helpText: String?
        val width: FormTemplateQuestionModel.Width
    }

    interface Update : UpdateSubrep {

        val label: String?
        val helpText: String?
        val width: FormTemplateQuestionModel.Width?

        override fun validate() {
            validate(Update::label) { ifPresent { mediumText(allowEmpty = false) } }
            validate(Update::helpText) { ifPresent { longText(allowEmpty = false) } }
        }
    }
}
