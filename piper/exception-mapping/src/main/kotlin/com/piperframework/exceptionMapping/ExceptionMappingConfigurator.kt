package com.piperframework.exceptionMapping

import io.ktor.features.StatusPages
import com.piperframework.exceptionMapping.exceptionMapper.ConflictException
import com.piperframework.exceptionMapping.exceptionMapper.ForbiddenException
import com.piperframework.exceptionMapping.exceptionMapper.InvalidTypeIdException
import com.piperframework.exceptionMapping.exceptionMapper.MissingKotlinParameterException
import com.piperframework.exceptionMapping.exceptionMapper.NotFoundException
import com.piperframework.exceptionMapping.exceptionMapper.ValidationError

/**
 * The ExceptionMappingConfigurator sets up exception mapping. Exception mapping refers to catching exceptions and
 * converting them to JSON responses.
 */
class ExceptionMappingConfigurator {

    fun configureExceptionMapping(configuration: StatusPages.Configuration) {
        configuration.exception(ConflictException().handler)
        configuration.exception(ForbiddenException().handler)
        configuration.exception(InvalidTypeIdException().handler)
        configuration.exception(MissingKotlinParameterException().handler)
        configuration.exception(NotFoundException().handler)
        configuration.exception(ValidationError().handler)
    }
}
