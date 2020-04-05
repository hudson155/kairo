package io.limberapp.backend.test

import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.serialization.Json
import com.piperframework.testing.AbstractResourceTest

abstract class LimberResourceTest : AbstractResourceTest() {

    protected val json by lazy {
        Json().apply {
            module.configureJson(this)
        }
    }

    abstract val module: Module

    abstract val additionalModules: Set<ModuleWithLifecycle>

    override val piperTest by lazy {
        LimberTest(json) {
            TestLimberApp(
                application = this,
                config = config,
                module = module,
                additionalModules = additionalModules,
                fixedClock = fixedClock,
                deterministicUuidGenerator = deterministicUuidGenerator
            )
        }
    }
}
