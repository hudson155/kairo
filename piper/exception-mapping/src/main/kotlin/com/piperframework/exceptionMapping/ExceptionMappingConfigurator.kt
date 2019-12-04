package com.piperframework.exceptionMapping

import com.piperframework.exceptionMapping.exceptionMapper.BadRequestExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.ConflictExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.ForbiddenExceptionMapper
import com.piperframework.exceptionMapping.exceptionMapper.NotFoundExceptionMapper
import io.ktor.features.StatusPages

/**
 * The ExceptionMappingConfigurator sets up exception mapping. Exception mapping refers to catching exceptions and
 * converting them to JSON responses.
 */
class ExceptionMappingConfigurator {

    fun configureExceptionMapping(configuration: StatusPages.Configuration) {
        configuration.exception(BadRequestExceptionMapper().handler)
        configuration.exception(ConflictExceptionMapper().handler)
        configuration.exception(ForbiddenExceptionMapper().handler)
        configuration.exception(NotFoundExceptionMapper().handler)
    }
}
