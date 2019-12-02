package com.piperframework.exceptionMapping

import com.piperframework.exceptionMapping.exceptionMapper.BadRequestException
import com.piperframework.exceptionMapping.exceptionMapper.ConflictException
import com.piperframework.exceptionMapping.exceptionMapper.ForbiddenException
import com.piperframework.exceptionMapping.exceptionMapper.NotFoundException
import io.ktor.features.StatusPages

/**
 * The ExceptionMappingConfigurator sets up exception mapping. Exception mapping refers to catching exceptions and
 * converting them to JSON responses.
 */
class ExceptionMappingConfigurator {

    fun configureExceptionMapping(configuration: StatusPages.Configuration) {
        configuration.exception(BadRequestException().handler)
        configuration.exception(ConflictException().handler)
        configuration.exception(ForbiddenException().handler)
        configuration.exception(NotFoundException().handler)
    }
}
