package io.limberapp.exception.feature

import io.limberapp.exception.ConflictException

class FeatureNameIsNotUnique : ConflictException(
    message = "The feature cannot have the same name as another feature.",
)
