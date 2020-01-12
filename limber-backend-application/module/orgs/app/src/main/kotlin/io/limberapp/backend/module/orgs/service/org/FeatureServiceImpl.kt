package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class FeatureServiceImpl @Inject constructor(
    @Store private val featureStore: FeatureService
) : FeatureService by featureStore
