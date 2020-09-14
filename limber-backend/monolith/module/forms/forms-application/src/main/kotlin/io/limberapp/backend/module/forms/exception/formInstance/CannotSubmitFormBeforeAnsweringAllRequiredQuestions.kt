package io.limberapp.backend.module.forms.exception.formInstance

import io.limberapp.common.exception.exception.conflict.ConflictException

internal class CannotSubmitFormBeforeAnsweringAllRequiredQuestions : ConflictException(
  message = "You can't submit this form until you answer all required questions.",
  developerMessage = "This exception should be thrown when an attempt is made to mark a form instance as submitted" +
    " but form instance questions don't exist for each form template question marked as required.")
