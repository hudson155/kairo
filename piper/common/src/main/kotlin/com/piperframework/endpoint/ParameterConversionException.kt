package com.piperframework.endpoint

import com.piperframework.dataConversion.DataConversionException
import com.piperframework.exception.exception.badRequest.BadRequestException

class ParameterConversionException(dataConversionException: DataConversionException) :
    BadRequestException(dataConversionException.message!!, dataConversionException)
