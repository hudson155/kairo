package io.limberapp.backend.module.auth.exception.feature

import com.piperframework.exception.exception.conflict.ConflictException

internal class FeatureRoleIsNotUnique : ConflictException(
  message = "The feature role already exists.",
  developerMessage = "This exception should be thrown when an attempt is made to create a feature role and there is" +
    " already a feature role for that feature with the same org role."
)
