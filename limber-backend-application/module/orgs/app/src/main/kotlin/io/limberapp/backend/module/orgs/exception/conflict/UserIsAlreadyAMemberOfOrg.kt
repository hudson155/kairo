package io.limberapp.backend.module.orgs.exception.conflict

import com.piperframework.exception.exception.conflict.ConflictException

internal class UserIsAlreadyAMemberOfOrg() : ConflictException(
    message = "The user is already a member of the organization.",
    developerMessage = "This exception should be thrown when an attempt is made to create a membership within an org" +
            " and there is already one for that user."
)
