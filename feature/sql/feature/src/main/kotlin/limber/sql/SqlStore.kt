package limber.sql

import com.google.common.io.Resources
import com.google.inject.Inject
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

/**
 * This class uses [Inject] combined with lateinit to access injection.
 * Constructor-based injection is typically preferred due to immutability,
 * but in this case we get the nice advantage of
 * not requiring each implementation to bloat its constructor.
 */
@Suppress("LateinitUsage")
public abstract class SqlStore {
  @Inject
  protected lateinit var jdbi: Jdbi

  @Suppress("FunctionMinLength") // Short for the purpose of brevity due to frequent reuse.
  protected fun rs(resourceName: String): String =
    Resources.getResource(resourceName).readText()
}

public fun <R> Jdbi.transaction(callback: (Handle) -> R): R =
  inTransaction<R, Exception>(callback)

public fun <R> Jdbi.handle(callback: (Handle) -> R): R =
  withHandle<R, Exception>(callback)
