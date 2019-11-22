package io.limberapp.framework.exceptionMapping

import io.ktor.features.StatusPages
import io.limberapp.framework.exceptionMapping.exceptionMapper.ConflictException
import io.limberapp.framework.exceptionMapping.exceptionMapper.ForbiddenException
import io.limberapp.framework.exceptionMapping.exceptionMapper.InvalidTypeIdException
import io.limberapp.framework.exceptionMapping.exceptionMapper.MissingKotlinParameterException
import io.limberapp.framework.exceptionMapping.exceptionMapper.NotFoundException
import io.limberapp.framework.exceptionMapping.exceptionMapper.ValidationError

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
