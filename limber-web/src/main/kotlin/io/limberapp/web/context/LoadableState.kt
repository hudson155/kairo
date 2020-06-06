package io.limberapp.web.context

internal sealed class LoadableState<State : Any> {
  abstract val hasBegunLoading: Boolean
  abstract val isLoaded: Boolean
  val isLoading get() = hasBegunLoading && !isLoaded

  abstract val stateOrNull: State?

  fun update(function: (State?) -> State) = (this as Loaded<State>).copy(state = function(state))

  internal data class Unloaded<State : Any>(override val hasBegunLoading: Boolean) : LoadableState<State>() {
    override val isLoaded = false
    override val stateOrNull: State? = null
  }

  internal data class Loaded<State : Any>(val state: State) : LoadableState<State>() {
    override val hasBegunLoading = true
    override val isLoaded = true
    override val stateOrNull: State = state
  }

  internal data class Error<State : Any>(private val errorMessage: String?) : LoadableState<State>() {
    override val hasBegunLoading = true
    override val isLoaded = true
    override val stateOrNull: State? = null
  }

  companion object {
    fun <State : Any> initial() = Unloaded<State>(hasBegunLoading = false)
    fun <State : Any> loading() = Unloaded<State>(hasBegunLoading = true)
  }
}
