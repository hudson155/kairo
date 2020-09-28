package io.limberapp.backend.module.auth.exception.org

import io.limberapp.exception.conflict.ConflictException

class AccountIsAlreadyMemberOfOrgRole : ConflictException(
  message = "The account is already a member of the org role.",
  developerMessage = "This exception should be thrown when an attempt is made to create an org role membership and" +
    " there is already an org role membership for the same org role and account."
)
