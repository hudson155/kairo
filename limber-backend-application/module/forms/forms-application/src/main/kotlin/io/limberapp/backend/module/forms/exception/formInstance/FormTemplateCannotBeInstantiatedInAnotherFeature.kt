package io.limberapp.backend.module.forms.exception.formInstance

import com.piperframework.exception.exception.conflict.ConflictException

internal class FormTemplateCannotBeInstantiatedInAnotherFeature : ConflictException(
  message = "A form instance cannot be created in an feature if the corresponding form template is in another" +
      " feature.",
  developerMessage = "This exception should be thrown when an attempt is made to create a form instance within an" +
      " feature using a form template that is not in that same feature."
)
