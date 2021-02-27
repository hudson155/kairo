package io.limberapp.exception.feature

import io.limberapp.exception.ConflictException

class FeatureRankIsNotUnique : ConflictException(
    message = "The feature cannot have the same rank as another feature.",
)
