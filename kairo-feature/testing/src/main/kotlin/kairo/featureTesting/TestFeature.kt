package kairo.featureTesting

import com.google.inject.Injector

public interface TestFeature {
  /**
   * [beforeEachTest] will be called before each test.
   */
  public interface BeforeEach {
    public suspend fun beforeEachTest(injector: Injector)
  }
}
