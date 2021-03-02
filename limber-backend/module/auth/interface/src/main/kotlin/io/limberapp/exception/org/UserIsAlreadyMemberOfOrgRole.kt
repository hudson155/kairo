package io.limberapp.exception.org

import io.limberapp.exception.ConflictException

class UserIsAlreadyMemberOfOrgRole : ConflictException(
    message = "The user is already a member of the org role.",
)
