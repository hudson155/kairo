package com.piperframework.exceptionMapping

import com.piperframework.error.PiperError
import com.piperframework.exception.PiperException
import com.piperframework.exception.exception.badRequest.BadRequestException
import com.piperframework.exception.exception.conflict.ConflictException
import com.piperframework.exception.exception.forbidden.ForbiddenException
import com.piperframework.exception.exception.notFound.NotFoundException
import com.piperframework.exceptionMapping.exceptionMapper.BadRequestExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.ConflictExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.ForbiddenExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.NotFoundExceptionMapper

class CompleteExceptionMapper : ExceptionMapper<PiperException>() {

    private val badRequestExceptionMapper = BadRequestExceptionMapper()
    private val conflictExceptionMapper = ConflictExceptionMapper()
    private val forbiddenExceptionMapper = ForbiddenExceptionMapper()
    private val notFoundExceptionMapper = NotFoundExceptionMapper()

    override fun handle(e: PiperException): PiperError {
        return when (e) {
            is BadRequestException -> badRequestExceptionMapper.handle(e)
            is ConflictException -> conflictExceptionMapper.handle(e)
            is ForbiddenException -> forbiddenExceptionMapper.handle(e)
            is NotFoundException -> notFoundExceptionMapper.handle(e)
            else -> error("Unknown exception type: ${e::class.simpleName}")
        }
    }
}
