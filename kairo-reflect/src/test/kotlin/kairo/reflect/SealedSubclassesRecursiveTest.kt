package kairo.reflect

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

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
  fun a() {
    A::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("C", "D", "F", "G", "I", "J")
  }

  @Test
  fun b() {
    B::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("F", "G", "I", "J")
  }

  @Test
  fun c() {
    C::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("C")
  }

  @Test
  fun d() {
    D::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("D")
  }

  @Test
  fun e() {
    E::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("I", "J")
  }

  @Test
  fun f() {
    F::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("F")
  }

  @Test
  fun g() {
    G::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("G")
  }

  @Test
  fun h() {
    H::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldBeEmpty()
  }

  @Test
  fun i() {
    I::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("I")
  }

  @Test
  fun j() {
    J::class.sealedSubclassesRecursive.map { it.simpleName!! }
      .shouldContainExactlyInAnyOrder("J")
  }
}
