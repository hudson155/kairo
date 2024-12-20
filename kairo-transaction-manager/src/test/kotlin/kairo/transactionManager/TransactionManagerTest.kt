package kairo.transactionManager

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.key
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnnecessaryInnerClass", "UNREACHABLE_CODE")
internal class TransactionManagerTest {
  private abstract inner class TestTransactionType : TransactionType(), TransactionType.WithContext {
    override suspend fun createContext(): CoroutineContext {
      nextCreateContextShouldThrow?.let { message ->
        nextCreateContextShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName!!} create context")
      return EmptyCoroutineContext
    }

    override suspend fun begin() {
      nextBeginShouldThrow?.let { message ->
        nextBeginShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName!!} begin transaction")
    }

    override suspend fun commit() {
      nextCommitShouldThrow?.let { message ->
        nextCommitShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName!!} commit transaction")
    }

    override suspend fun rollback() {
      nextRollbackShouldThrow?.let { message ->
        nextRollbackShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName!!} rollback transaction")
    }

    override suspend fun closeContext(context: CoroutineContext) {
      nextCloseContextShouldThrow?.let { message ->
        nextCloseContextShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName!!} close context")
    }
  }

  private inner class Type0 : TestTransactionType()

  private inner class Type1 : TestTransactionType()

  private inner class Type2 : TestTransactionType()

  private val injector: Injector =
    Guice.createInjector(
      object : AbstractModule() {
        override fun configure() {
          binder().bind<Type0>().toInstance(Type0())
          binder().bind<Type1>().toInstance(Type1())
          binder().bind<Type2>().toInstance(Type2())
        }
      },
    )

  private val transactionManager: TransactionManager = TransactionManager(injector)

  private var nextCreateContextShouldThrow: String? = null
  private var nextBeginShouldThrow: String? = null
  private var nextCommitShouldThrow: String? = null
  private var nextRollbackShouldThrow: String? = null
  private var nextCloseContextShouldThrow: String? = null

  private val events: MutableList<String> = mutableListOf()

  @Test
  fun `single transaction, successful`(): Unit = runTest {
    transactionManager.transaction(key<Type0>(), key<Type1>()) {
      events.add("Operation 0")
    }
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 commit transaction",
      "Type0 commit transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, rollback`(): Unit = runTest {
    shouldThrow<IllegalStateException> {
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
        error("Error 0")
      }
    }.shouldHaveMessage("Error 0")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 rollback transaction",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `consecutive transactions, successful`(): Unit = runTest {
    transactionManager.transaction(key<Type0>(), key<Type1>()) {
      events.add("Operation 0")
    }
    transactionManager.transaction(key<Type0>(), key<Type2>()) {
      events.add("Operation 1")
    }
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 commit transaction",
      "Type0 commit transaction",
      "Type1 close context",
      "Type0 close context",
      "Type0 create context",
      "Type2 create context",
      "Type0 begin transaction",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 commit transaction",
      "Type0 commit transaction",
      "Type2 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `consecutive transactions, rollback`(): Unit = runTest {
    shouldThrow<IllegalStateException> {
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
        error("Error 0")
      }
    }.shouldHaveMessage("Error 0")
    shouldThrow<IllegalStateException> {
      transactionManager.transaction<Unit>(key<Type0>(), key<Type2>()) {
        events.add("Operation 1")
        error("Error 1")
      }
    }.shouldHaveMessage("Error 1")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 rollback transaction",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
      "Type0 create context",
      "Type2 create context",
      "Type0 begin transaction",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 rollback transaction",
      "Type0 rollback transaction",
      "Type2 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `nested transactions, successful`(): Unit = runTest {
    transactionManager.transaction(key<Type0>(), key<Type1>()) {
      events.add("Operation 0")
      transactionManager.transaction(key<Type0>(), key<Type2>()) {
        events.add("Operation 1")
      }
      events.add("Operation 2")
    }
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type2 create context",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 commit transaction",
      "Type2 close context",
      "Operation 2",
      "Type1 commit transaction",
      "Type0 commit transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `nested transactions, inner rollback not caught`(): Unit = runTest {
    val e = shouldThrow<IllegalStateException> {
      transactionManager.transaction(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
        transactionManager.transaction(key<Type0>(), key<Type2>()) {
          events.add("Operation 1")
          error("Error 1")
        }
        events.add("Operation 2")
      }
    }
    e.shouldHaveMessage("Error 1")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type2 create context",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 rollback transaction",
      "Type2 close context",
      "Type1 rollback transaction",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `nested transactions, inner rollback caught`(): Unit = runTest {
    transactionManager.transaction(key<Type0>(), key<Type1>()) {
      events.add("Operation 0")
      try {
        transactionManager.transaction(key<Type0>(), key<Type2>()) {
          events.add("Operation 1")
          error("Error 1")
        }
      } catch (_: IllegalStateException) {
      }
      events.add("Operation 2")
    }
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type2 create context",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 rollback transaction",
      "Type2 close context",
      "Operation 2",
      "Type1 commit transaction",
      "Type0 commit transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `nested transactions, outer rollback`(): Unit = runTest {
    shouldThrow<IllegalStateException> {
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
        transactionManager.transaction(key<Type0>(), key<Type2>()) {
          events.add("Operation 1")
        }
        events.add("Operation 2")
        error("Error 2")
      }
    }.shouldHaveMessage("Error 2")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type2 create context",
      "Type2 begin transaction",
      "Operation 1",
      "Type2 commit transaction",
      "Type2 close context",
      "Operation 2",
      "Type1 rollback transaction",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, error during create context`(): Unit = runTest {
    shouldThrow<TransactionContextCreationFailedException> {
      nextCreateContextShouldThrow = "Create context error"
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
      }
    }.cause.shouldBeInstanceOf<IllegalStateException>().shouldHaveMessage("Create context error")
    events.shouldBeEmpty()
  }

  @Test
  fun `single transaction, error during begin`(): Unit = runTest {
    shouldThrow<TransactionBeginFailedException> {
      nextBeginShouldThrow = "Begin error"
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
      }
    }.cause.shouldBeInstanceOf<IllegalStateException>().shouldHaveMessage("Begin error")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, error during commit`(): Unit = runTest {
    shouldThrow<TransactionCommitFailedException> {
      nextCommitShouldThrow = "Commit error"
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
      }
    }.cause.shouldBeInstanceOf<IllegalStateException>().shouldHaveMessage("Commit error")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 rollback transaction",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, error during rollback`(): Unit = runTest {
    shouldThrow<IllegalStateException> {
      nextRollbackShouldThrow = "Rollback error"
      transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
        events.add("Operation 0")
        error("Error 0")
      }
    }.shouldHaveMessage("Error 0")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type0 rollback transaction",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, error during close context`(): Unit = runTest {
    nextCloseContextShouldThrow = "Close context error"
    transactionManager.transaction<Unit>(key<Type0>(), key<Type1>()) {
      events.add("Operation 0")
    }
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type0 begin transaction",
      "Type1 begin transaction",
      "Operation 0",
      "Type1 commit transaction",
      "Type0 commit transaction",
      "Type0 close context",
    )
  }
}
