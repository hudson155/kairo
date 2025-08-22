package kairo.rest.exceptionHandler

import com.google.inject.Binder
import kairo.dependencyInjection.bind
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.server.bindRestHandlers

internal class ExceptionHandlerTestFeature(
  private val libraryBookService: LibraryBookService,
) : Feature() {
  override val name: String = "Exception Handler Test"

  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bind<LibraryBookService>().toInstance(libraryBookService)
    binder.bindRestHandlers<ExceptionHandlerLibraryBookHandler>()
  }
}
