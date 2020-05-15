package com.piperframework.module

import com.google.inject.AbstractModule

/**
 * An module that can be unconfigured to release resources when it's no longer needed.
 */
abstract class ModuleWithLifecycle : AbstractModule() {
  abstract fun unconfigure()
}
