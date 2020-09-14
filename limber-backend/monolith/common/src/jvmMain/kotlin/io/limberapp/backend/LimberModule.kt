package io.limberapp.backend

object LimberModule {

  @RequiresOptIn
  @Retention(AnnotationRetention.BINARY)
  @Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION)
  annotation class Auth

  @RequiresOptIn
  @Retention(AnnotationRetention.BINARY)
  @Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION)
  annotation class Forms

  @RequiresOptIn
  @Retention(AnnotationRetention.BINARY)
  @Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION)
  annotation class Orgs

  @RequiresOptIn
  @Retention(AnnotationRetention.BINARY)
  @Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION)
  annotation class Users
}
