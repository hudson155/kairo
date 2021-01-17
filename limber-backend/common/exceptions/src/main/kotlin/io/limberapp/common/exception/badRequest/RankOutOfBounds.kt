package io.limberapp.common.exception.badRequest

class RankOutOfBounds(rank: Int) : BadRequestException(
    message = "The given rank \"$rank\" is out of bounds.",
    userVisibleProperties = mapOf("rank" to rank),
)
