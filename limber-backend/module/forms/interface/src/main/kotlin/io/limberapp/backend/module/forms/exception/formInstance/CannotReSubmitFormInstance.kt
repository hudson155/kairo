package io.limberapp.backend.module.forms.exception.formInstance

import io.limberapp.exception.conflict.ConflictException

class CannotReSubmitFormInstance : ConflictException(
  message = "This form has already been submitted.",
  developerMessage = "This exception should be thrown when an attempt is made to mark a form instance as submitted" +
    " but form instance is already submitted.")
