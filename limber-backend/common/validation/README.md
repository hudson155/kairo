# `limber-backend:common:validation`

A dependency-less module that handles simple syntactic input validation.
All validation methods herein should run quickly.

Most validation methods are Regex or length-based.

Validation methods should usually not be domain-specific.
For example, rather than letting `orgName` be its own validation method,
`shortText` should be its own validation method and should be used when validating the org name.
