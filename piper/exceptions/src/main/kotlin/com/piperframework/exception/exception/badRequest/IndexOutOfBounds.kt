package com.piperframework.exception.exception.badRequest

class IndexOutOfBounds(index: Short) : BadRequestException("The given index \"$index\" is out of bounds.")
