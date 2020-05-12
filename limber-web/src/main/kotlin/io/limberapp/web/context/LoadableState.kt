package io.limberapp.web.context

internal data class LoadableState<State : Any>(val loadingStatus: LoadingStatus, val state: State?) {
    enum class LoadingStatus { INITIAL, LOADING, LOADED }

    val hasBegunLoading = loadingStatus != LoadingStatus.INITIAL

    val isLoaded = loadingStatus == LoadingStatus.LOADED

    fun loading() = copy(loadingStatus = LoadingStatus.LOADING)

    fun loaded(state: State) = copy(
        loadingStatus = LoadingStatus.LOADED,
        state = state
    )

    fun update(function: (State?) -> State) = copy(state = function(state))

    companion object {
        fun <State : Any> initial() = LoadableState<State>(LoadingStatus.INITIAL, null)
    }
}
