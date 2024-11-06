package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint

public abstract class TaskCreator {
  @Target(AnnotationTarget.CLASS)
  public annotation class Queue(val name: String)

  public abstract suspend fun <E : RestEndpoint<I, *>, I : Any> create(endpoint: E)
}
