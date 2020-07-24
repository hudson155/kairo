# Limber

Limber is a highly dynamic application built on Ktor and React.

## Modules

* [`auth`](module/auth)
* [`forms`](module/forms)
* [`orgs`](module/orgs)
* [`users`](module/users)

## Soft Deleted Entities

This is a list of soft deleted entities, kept here for reasons of data retention.
Entities in this list should be hard-deleted after some retention period.

* `forms.form_instance`
* `forms.form_template`
* `orgs.feature`
* `orgs.org`
* `orgs.org_role`
* `users.account`
