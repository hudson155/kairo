package io.limberapp.backend.module.forms.exception.formInstance

import io.limberapp.exception.conflict.ConflictException

internal class CannotDeleteRequiredQuestion : ConflictException(
  message = "You can't delete this question - it is required. Update it instead.",
  developerMessage = "This exception should be thrown when an attempt is made to delete a form instance question for" +
    " which the corresponding form template question is required.")
