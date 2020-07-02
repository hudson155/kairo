package io.limberapp.web.api

import io.limberapp.web.util.async
import react.*

internal fun <T : Any> load(function: suspend () -> Result<T>): Result<T>? {
  val (state, setState) = useState<Result<T>?>(null)
  useEffect(emptyList()) { async { setState(function()) } }
  return state
}
