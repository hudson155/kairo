package com.piperframework.restInterface

@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
class FetchFailure(val status: Short) : Exception("Failed to fetch with response status $status.")
