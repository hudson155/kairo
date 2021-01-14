# Validation

A **dependency-less** library that handles simple syntactic input validation.
All validation methods herein should run quickly.

Most validation methods are Regex or length-based.

Validation methods should domain-specific when frequently used.
For example `orgName` should be its own validation method
rather than using `shortText`.
