package io.limberapp.backend.module.users.exception.account

import com.piperframework.exception.exception.conflict.ConflictException

internal class CannotDeleteUserWithOrgs : ConflictException(
    message = "You can't delete your account because you're the owner of one or more organizations." +
            " Delete those organizations or transfer them to another owner.",
    developerMessage = "This exception should be thrown when an attempt is made to delete a user who is the owner of" +
            "at least 1 org."
)
