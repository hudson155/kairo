package kairo.reflect

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("FunctionMinLength")
internal class SealedSubclassesRecursiveTest {
  sealed class A

  sealed class B : A()

  class C : A()

  object D : A()

  sealed class E : B()

  class F : B()

  class G : B()

  sealed class H : E()

  class I : E()

  class J : E()

  @Test
  fun a(): Unit = runTest {
    A::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("C", "D", "F", "G", "I", "J")
  }

  @Test
  fun b(): Unit = runTest {
    B::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("F", "G", "I", "J")
  }

  @Test
  fun c(): Unit = runTest {
    C::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("C")
  }

  @Test
  fun d(): Unit = runTest {
    D::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("D")
  }

  @Test
  fun e(): Unit = runTest {
    E::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("I", "J")
  }

  @Test
  fun f(): Unit = runTest {
    F::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("F")
  }

  @Test
  fun g(): Unit = runTest {
    G::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("G")
  }

  @Test
  fun h(): Unit = runTest {
    H::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldBeEmpty()
  }

  @Test
  fun i(): Unit = runTest {
    I::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("I")
  }

  @Test
  fun j(): Unit = runTest {
    J::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("J")
  }
}
