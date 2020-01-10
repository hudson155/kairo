package com.piperframework.exception.exception.badRequest

class IndexOutOfBounds(index: Int) : BadRequestException("The given index \"$index\" is out of bounds.")
