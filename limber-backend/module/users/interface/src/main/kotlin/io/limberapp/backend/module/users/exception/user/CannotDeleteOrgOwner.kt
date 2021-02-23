package io.limberapp.backend.module.users.exception.user

import io.limberapp.common.exception.conflict.ConflictException

class CannotDeleteOrgOwner : ConflictException(
    message = "You can't delete a user that is the owner of an organization." +
        " Delete the organization instead, or transfer it to another owner.",
)
