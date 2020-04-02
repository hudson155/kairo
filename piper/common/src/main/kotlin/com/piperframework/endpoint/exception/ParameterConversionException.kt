package com.piperframework.endpoint.exception

import com.piperframework.dataConversion.DataConversionException
import com.piperframework.exception.exception.badRequest.BadRequestException

class ParameterConversionException(dataConversionException: DataConversionException) :
    BadRequestException(dataConversionException.message!!, dataConversionException)
