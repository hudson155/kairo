package io.limberapp.web.api

import com.piperframework.util.Outcome
import io.limberapp.web.util.async
import react.*

internal fun <T : Any> load(function: suspend () -> Outcome<T>): Outcome<T>? {
  val (state, setState) = useState<Outcome<T>?>(null)
  useEffect(emptyList()) { async { setState(function()) } }
  return state
}
