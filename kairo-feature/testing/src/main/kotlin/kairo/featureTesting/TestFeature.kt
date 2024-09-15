package kairo.featureTesting

import com.google.inject.Injector

public interface TestFeature {
  public interface BeforeEach {
    public suspend fun beforeEachTest(injector: Injector)
  }
}
