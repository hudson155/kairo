package io.limberapp.backend.module.forms.rep.formTemplate

import com.piperframework.rep.CompleteRep
import com.piperframework.rep.CreationRep
import com.piperframework.rep.UpdateRep
import com.piperframework.types.UUID
import com.piperframework.validation.RepValidation
import com.piperframework.validation.ifPresent
import com.piperframework.validator.Validator

object FormTemplateQuestionRep {

    interface Creation : CreationRep {

        val label: String
        val helpText: String?

        override fun validate() = RepValidation {
            validate(Creation::label) { Validator.length1hundred(value, allowEmpty = false) }
            validate(Creation::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
        }
    }

    interface Complete : CompleteRep {
        val id: UUID
        val label: String
        val helpText: String?
    }

    interface Update : UpdateRep {

        val label: String?
        val helpText: String?

        override fun validate() = RepValidation {
            validate(Update::label) { ifPresent { Validator.length1hundred(value, allowEmpty = false) } }
            validate(Update::helpText) { ifPresent { Validator.length10thousand(value, allowEmpty = false) } }
        }
    }
}
