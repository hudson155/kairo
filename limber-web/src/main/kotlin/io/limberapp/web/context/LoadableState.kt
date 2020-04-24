package io.limberapp.web.context

internal data class LoadableState<State : Any>(val loadingStatus: LoadingStatus, val state: State?) {
    enum class LoadingStatus { INITIAL, LOADING, LOADED }

    val hasBegunLoading = loadingStatus != LoadingStatus.INITIAL

    val isLoaded = loadingStatus == LoadingStatus.LOADED

    companion object {
        fun <State : Any> initial() = LoadableState<State>(LoadingStatus.INITIAL, null)
    }
}
