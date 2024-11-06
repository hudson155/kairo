package kairo.googleCloudTasks

import kairo.rest.endpoint.RestEndpoint

/**
 * Primarily, [RealTaskCreator].
 */
public abstract class TaskCreator {
  @Target(AnnotationTarget.CLASS)
  public annotation class Queue(val name: String)

  public abstract suspend fun create(endpoint: RestEndpoint<*, *>)
}
