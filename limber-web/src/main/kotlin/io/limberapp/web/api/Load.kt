package io.limberapp.web.api

import com.piperframework.util.Outcome
import io.limberapp.web.util.async
import react.*

internal fun <T : Any> load(
  dependencies: RDependenciesList = emptyList(),
  function: suspend () -> Outcome<T>?,
): Outcome<T>? {
  val (state, setState) = useState<Outcome<T>?>(null)
  useEffect(dependencies) { async { setState(function()) } }
  return state
}
