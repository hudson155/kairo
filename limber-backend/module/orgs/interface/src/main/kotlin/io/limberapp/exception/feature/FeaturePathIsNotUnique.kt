package io.limberapp.exception.feature

import io.limberapp.exception.ConflictException

class FeaturePathIsNotUnique : ConflictException(
    message = "The feature cannot have the same path as another feature.",
)
