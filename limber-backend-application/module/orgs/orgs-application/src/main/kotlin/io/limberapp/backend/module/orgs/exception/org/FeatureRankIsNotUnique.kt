package io.limberapp.backend.module.orgs.exception.org

import com.piperframework.exception.exception.conflict.ConflictException

internal class FeatureRankIsNotUnique : ConflictException(
  message = "The feature cannot have the same r as another feature.",
  developerMessage = "This exception should be thrown when an attempt is made to create or update a feature within an" +
    " org and there is already a feature with the same rank."
)
