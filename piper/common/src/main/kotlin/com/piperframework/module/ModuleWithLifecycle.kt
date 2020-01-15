package com.piperframework.module

import com.google.inject.AbstractModule

abstract class ModuleWithLifecycle : AbstractModule() {

    abstract fun unconfigure()
}
