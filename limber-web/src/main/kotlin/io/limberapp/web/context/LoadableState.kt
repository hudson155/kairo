package io.limberapp.web.context

internal data class LoadableState<S : Any>(val loadingStatus: LoadingStatus, val state: S?) {

    enum class LoadingStatus { INITIAL, LOADING, LOADED }

    val hasBegunLoading = loadingStatus != LoadingStatus.INITIAL

    val isLoaded = loadingStatus == LoadingStatus.LOADED

    companion object {
        fun <S : Any> initial() = LoadableState<S>(LoadingStatus.INITIAL, null)
    }
}
