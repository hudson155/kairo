package limber.feature.sql

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import limber.testing.IntegrationTest
import limber.testing.test
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.transaction.TransactionException
import org.junit.jupiter.api.Test
import java.sql.Connection
import kotlin.coroutines.coroutineContext

internal class SqlTest : IntegrationTest() {
  @Test
  fun `identity of connections`() {
    test {
      var connection0: Connection?
      sql.sql {
        connection0 = getHandle().shouldNotBeNull().connection
        connection0.shouldNotBeNull()
        sql.sql {
          val connection1 = getHandle().shouldNotBeNull().connection
          connection1.shouldBeSameInstanceAs(connection0)
        }
      }
    }
  }

  @Test
  fun `identity of handles`() {
    test {
      val handle0 = getHandle()
      handle0.shouldBe(null)
      var handle1: Handle? = null
      sql.sql { handle2 ->
        handle2.shouldNotBeNull()
        handle1 = getHandle()
        handle1.shouldNotBeNull()
        handle1.shouldBeSameInstanceAs(handle2)
        sql.sql { handle4 ->
          handle4.shouldNotBeNull()
          val handle3 = getHandle()
          handle3.shouldNotBeNull()
          handle3.shouldBeSameInstanceAs(handle4)
          handle3.shouldBeSameInstanceAs(handle1)
        }
      }
      val handle5 = getHandle()
      handle5.shouldBe(null)
      var handle6: Handle?
      sql.sql {
        handle6 = getHandle()
        handle6.shouldNotBeSameInstanceAs(handle1)
      }
    }
  }

  @Test
  fun `handle status`() {
    test {
      var handle: Handle? = null
      sql.sql {
        handle = getHandle()
        with(handle.shouldNotBeNull()) {
          isClosed.shouldBeFalse()
          isInTransaction.shouldBeTrue()
        }
      }
      with(handle.shouldNotBeNull()) {
        isClosed.shouldBeTrue()
        val e = shouldThrow<TransactionException> {
          isInTransaction
        }
        e.shouldHaveMessage("Failed to test for transaction status")
      }
    }
  }

  @Test
  fun `transaction commit`() {
    fun Handle.getIds(): List<String> =
      createQuery("select id from testing.entity").mapTo(String::class.java).toList()

    fun Handle.insertId(id: String) =
      createUpdate("insert into testing.entity (id) values (:id)").bind("id", id).execute()

    test {
      sql.sql { handle ->
        handle.getIds().shouldBeEmpty()
        handle.insertId("ent_0")
        handle.getIds().shouldContainExactlyInAnyOrder("ent_0")
      }
      sql.sql { handle ->
        handle.getIds().shouldContainExactlyInAnyOrder("ent_0")
      }
    }
  }

  @Test
  fun `transaction rollback`() {
    fun Handle.getIds(): List<String> =
      createQuery("select id from testing.entity").mapTo(String::class.java).toList()

    fun Handle.insertId(id: String) =
      createUpdate("insert into testing.entity (id) values (:id)").bind("id", id).execute()

    test {
      try {
        sql.sql { handle ->
          handle.getIds().shouldBeEmpty()
          handle.insertId("ent_0")
          handle.getIds().shouldContainExactlyInAnyOrder("ent_0")
          error("This should roll back the transaction.")
        }
      } catch (e: Exception) {
        e.shouldHaveMessage("This should roll back the transaction.")
      }
      sql.sql { handle ->
        handle.getIds().shouldBeEmpty()
      }
    }
  }

  private suspend fun getHandle(): Handle? =
    coroutineContext[SqlContext]?.handle
}
