package io.limberapp.exception.feature

import io.limberapp.exception.ConflictException

class FeatureRoleOrgRoleIsNotUnique : ConflictException(
    message = "The feature role already exists.",
)
