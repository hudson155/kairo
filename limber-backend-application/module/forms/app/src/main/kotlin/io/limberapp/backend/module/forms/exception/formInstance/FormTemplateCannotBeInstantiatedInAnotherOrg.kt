package io.limberapp.backend.module.forms.exception.formInstance

import com.piperframework.exception.exception.conflict.ConflictException

internal class FormTemplateCannotBeInstantiatedInAnotherOrg : ConflictException(
    message = "A form instance cannot be created in an org if the corresponding form template is in another org.",
    developerMessage = "This exception should be thrown when an attempt is made to create a form instance within an" +
            " org using a form template that is not in that same org."
)
