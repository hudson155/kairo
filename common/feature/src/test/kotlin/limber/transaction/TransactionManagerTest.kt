package limber.transaction

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("UnnecessaryInnerClass", "UNREACHABLE_CODE")
internal class TransactionManagerTest {
  private abstract inner class TestTransactionType : TransactionType(), TransactionType.WithContext {
    override suspend fun createContext(): CoroutineContext {
      nextCreateContextShouldThrow?.let { message ->
        nextCreateContextShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName} create context")
      return EmptyCoroutineContext
    }

    override suspend fun begin() {
      nextBeginShouldThrow?.let { message ->
        nextBeginShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName} begin transaction")
    }

    override suspend fun commit() {
      nextCommitShouldThrow?.let { message ->
        nextCommitShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName} commit transaction")
    }

    override suspend fun rollback() {
      nextRollbackShouldThrow?.let { message ->
        nextRollbackShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName} rollback transaction")
    }

    override suspend fun closeContext(context: CoroutineContext) {
      nextCloseContextShouldThrow?.let { message ->
        nextCloseContextShouldThrow = null
        error(message)
      }
      events.add("${this::class.simpleName} close context")
    }
  }

  private inner class Type0 : TestTransactionType()

  private inner class Type1 : TestTransactionType()

  private inner class Type2 : TestTransactionType()

  private val injector: Injector =
    Guice.createInjector(
      object : AbstractModule() {
        override fun configure() {
          bind(Type0::class.java).toInstance(Type0())
          bind(Type1::class.java).toInstance(Type1())
          bind(Type2::class.java).toInstance(Type2())
        }
      },
    )

  private val manager = TransactionManager(injector)

  private var nextCreateContextShouldThrow: String? = null
  private var nextBeginShouldThrow: String? = null
  private var nextCommitShouldThrow: String? = null
  private var nextRollbackShouldThrow: String? = null
  private var nextCloseContextShouldThrow: String? = null

  private val events: MutableList<String> = mutableListOf()

  @Test
  fun `single transaction, successful`() {
    runBlocking {
      manager.transaction(Type0::class, Type1::class) {
        events.add("Operation 0")
      }
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
  fun `single transaction, rollback`() {
    runBlocking {
      val e = shouldThrow<IllegalStateException> {
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
          error("Error 0")
        }
      }
      e.shouldHaveMessage("Error 0")
    }
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
  fun `consecutive transactions, successful`() {
    runBlocking {
      manager.transaction(Type0::class, Type1::class) {
        events.add("Operation 0")
      }
    }
    runBlocking {
      manager.transaction(Type0::class, Type2::class) {
        events.add("Operation 1")
      }
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
  fun `consecutive transactions, rollback`() {
    runBlocking {
      val e = shouldThrow<IllegalStateException> {
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
          error("Error 0")
        }
      }
      e.shouldHaveMessage("Error 0")
    }
    runBlocking {
      val e = shouldThrow<IllegalStateException> {
        manager.transaction<Unit>(Type0::class, Type2::class) {
          events.add("Operation 1")
          error("Error 1")
        }
      }
      e.shouldHaveMessage("Error 1")
    }
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
  fun `nested transactions, successful`() {
    runBlocking {
      manager.transaction(Type0::class, Type1::class) {
        events.add("Operation 0")
        manager.transaction(Type0::class, Type2::class) {
          events.add("Operation 1")
        }
        events.add("Operation 2")
      }
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
  fun `nested transactions, inner rollback not caught`() {
    val e = shouldThrow<IllegalStateException> {
      runBlocking {
        manager.transaction(Type0::class, Type1::class) {
          events.add("Operation 0")
          manager.transaction(Type0::class, Type2::class) {
            events.add("Operation 1")
            error("Error 1")
          }
          events.add("Operation 2")
        }
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
  fun `nested transactions, inner rollback caught`() {
    runBlocking {
      manager.transaction(Type0::class, Type1::class) {
        events.add("Operation 0")
        try {
          manager.transaction(Type0::class, Type2::class) {
            events.add("Operation 1")
            error("Error 1")
          }
        } catch (_: IllegalStateException) {
        }
        events.add("Operation 2")
      }
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
  fun `nested transactions, outer rollback`() {
    val e = shouldThrow<IllegalStateException> {
      runBlocking {
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
          manager.transaction(Type0::class, Type2::class) {
            events.add("Operation 1")
          }
          events.add("Operation 2")
          error("Error 2")
        }
      }
    }
    e.shouldHaveMessage("Error 2")
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
  fun `single transaction, error during create context`() {
    val e = shouldThrow<TransactionContextCreationFailedException> {
      runBlocking {
        nextCreateContextShouldThrow = "Create context error"
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
        }
      }
    }
    e.cause.shouldNotBeNull().shouldHaveMessage("Create context error")
    events.shouldBeEmpty()
  }

  @Test
  fun `single transaction, error during begin`() {
    val e = shouldThrow<TransactionBeginFailedException> {
      runBlocking {
        nextBeginShouldThrow = "Begin error"
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
        }
      }
    }
    e.cause.shouldNotBeNull().shouldHaveMessage("Begin error")
    events.shouldContainExactly(
      "Type0 create context",
      "Type1 create context",
      "Type1 close context",
      "Type0 close context",
    )
  }

  @Test
  fun `single transaction, error during commit`() {
    val e = shouldThrow<TransactionCommitFailedException> {
      runBlocking {
        nextCommitShouldThrow = "Commit error"
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
        }
      }
    }
    e.cause.shouldNotBeNull().shouldHaveMessage("Commit error")
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
  fun `single transaction, error during rollback`() {
    val e = shouldThrow<IllegalStateException> {
      runBlocking {
        nextRollbackShouldThrow = "Rollback error"
        manager.transaction<Unit>(Type0::class, Type1::class) {
          events.add("Operation 0")
          error("Error 0")
        }
      }
    }
    e.shouldHaveMessage("Error 0")
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
  fun `single transaction, error during close context`() {
    runBlocking {
      nextCloseContextShouldThrow = "Close context error"
      manager.transaction<Unit>(Type0::class, Type1::class) {
        events.add("Operation 0")
      }
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
