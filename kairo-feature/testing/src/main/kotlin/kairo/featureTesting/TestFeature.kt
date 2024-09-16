package kairo.featureTesting

import com.google.inject.Injector

/**
 * Test Feature interfaces allow Features to be extended to hook into the test lifecycle.
 */
public interface TestFeature {
  public interface BeforeEach {
    public suspend fun beforeEach(injector: Injector)
  }
}
