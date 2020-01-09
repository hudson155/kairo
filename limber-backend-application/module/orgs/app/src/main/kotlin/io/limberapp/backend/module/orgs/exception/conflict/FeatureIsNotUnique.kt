package io.limberapp.backend.module.orgs.exception.conflict

import com.piperframework.exception.exception.conflict.ConflictException

internal class FeatureIsNotUnique : ConflictException(
    message = "The feature cannot have the same path as another feature.",
    developerMessage = "This exception should be thrown when an attempt is made to create a feature within an org and" +
            " and there is already a feature with the same path."
)
