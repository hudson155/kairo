package com.piperframework.exception.exception.badRequest

class RankOutOfBounds(rank: Int) : BadRequestException("The given rank \"$rank\" is out of bounds.")
