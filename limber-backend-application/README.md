# Limber

Limber is a highly dynamic application built on Ktor and React.

## Modules

* [`auth`](auth)
* [`forms`](forms)
* [`orgs`](orgs)
* [`users`](users)

## Soft Deleted Entities

This is a list of soft deleted entities, kept here for reasons of data retention.
Entities in this list should be hard-deleted after some retention period.

* `users.account`
* `orgs.feature`
* `orgs.org`
* `orgs.org_role`
