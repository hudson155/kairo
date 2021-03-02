package io.limberapp.exception.org

import io.limberapp.exception.ConflictException

class OrgRoleNameIsNotUnique : ConflictException(
    message = "The org role cannot have the same name as another org role.",
)
