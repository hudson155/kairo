package io.limberapp.backend.module.auth.exception.org

import com.piperframework.exception.exception.conflict.ConflictException

internal class OrgRoleIsNotUnique : ConflictException(
  message = "The role cannot have the same name as another role.",
  developerMessage = "This exception should be thrown when an attempt is made to create a role within an org and" +
      " there is already a role with the same name."
)
