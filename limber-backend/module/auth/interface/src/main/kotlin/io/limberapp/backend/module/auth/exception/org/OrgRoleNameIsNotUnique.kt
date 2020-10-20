package io.limberapp.backend.module.auth.exception.org

import io.limberapp.common.exception.conflict.ConflictException

class OrgRoleNameIsNotUnique : ConflictException(
    message = "The role cannot have the same name as another role.",
    developerMessage = "This exception should be thrown when an attempt is made to create an org role and there is" +
        " already an org role for that org with the same name."
)
