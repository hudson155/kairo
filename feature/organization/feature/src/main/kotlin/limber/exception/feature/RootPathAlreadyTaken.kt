package limber.exception.feature

import limber.exception.ConflictException

internal class RootPathAlreadyTaken : ConflictException("Root path already taken.")
