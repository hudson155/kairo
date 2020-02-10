package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.store.org.FeatureStore

internal class FeatureServiceImpl @Inject constructor(
    featureStore: FeatureStore
) : FeatureService by featureStore
