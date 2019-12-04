package com.piperframework.exceptionMapping.exceptionMapper

import com.piperframework.error.error.NotFoundError
import com.piperframework.exception.exception.notFound.NotFoundException
import com.piperframework.exceptionMapping.ExceptionMapper

internal class NotFoundException : ExceptionMapper<NotFoundException>() {
    override fun handle(e: NotFoundException) = NotFoundError()
}
