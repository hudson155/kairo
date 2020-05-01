# Limber

Limber is a highly dynamic application built on Ktor and React.

## Modules

* [`auth`](auth)
* [`forms`](forms)
* [`orgs`](orgs)
* [`users`](users)

## Endpoints

| Name                                  | Path Template                                                 |
| :------------------------------------ | :----------------------------------------------------------   |
| FormInstanceApi.Post                  | /form-instances                                               |
| FormInstanceApi.GetByFeatureId        | /form-instances?featureGuid={featureGuid}                     |
| FormInstanceApi.Get                   | /form-instances/{formInstanceGuid}                            |
| FormInstanceApi.Delete                | /form-instances/{formInstanceGuid}                            |
| FormInstanceQuestionApi.Put           | /form-instances/{formInstanceGuid}/questions/{questionGuid}   |
| FormInstanceQuestionApi.Delete        | /form-instances/{formInstanceGuid}/questions/{questionGuid}   |
| FormTemplateApi.Post                  | /form-templates                                               |
| FormTemplateApi.GetByFeatureId        | /form-templates?featureGuid={featureGuid}                     |
| FormTemplateApi.Get                   | /form-templates/{formTemplateGuid}                            |
| FormTemplateApi.Patch                 | /form-templates/{formTemplateGuid}                            |
| FormInstanceApi.Delete                | /form-templates/{formTemplateGuid}                            |
| FormTemplateQuestionApi.Post          | /form-templates/{formTemplateGuid}/questions                  |
| FormTemplateQuestionApi.Patch         | /form-templates/{formTemplateGuid}/questions/{questionGuid}   |
| FormTemplateQuestionApi.Delete        | /form-templates/{formTemplateGuid}/questions/{questionGuid}   |
| HealthCheckApi.Get                    | /health-cheeck                                                |
| JwtClaimsRequestApi.Post              | /jwt-claims-request                                           |
| OrgApi.Post                           | /orgs                                                         |
| OrgApi.GetByOwnerAccountGuid          | /orgs?ownerAccountGuid={ownerAccountGuid}                     |
| OrgApi.Get                            | /orgs/{orgGuid}                                               |
| OrgApi.Patch                          | /orgs/{orgGuid}                                               |
| OrgApi.Delete                         | /orgs/{orgGuid}                                               |
| OrgFeatureApi.Post                    | /orgs/{orgGuid}/features                                      |
| OrgFeatureApi.Patch                   | /orgs/{orgGuid}/features/{featureGuid}                        |
| OrgFeatureApi.Delete                  | /orgs/{orgGuid}/features/{featureGuid}                        |
| OrgRoleApi.Post                       | /orgs/{orgGuid}/roles                                         |
| OrgRoleApi.GetByOrgGuid               | /orgs/{orgGuid}/roles                                         |
| OrgRoleApi.Patch                      | /orgs/{orgGuid}/roles/{orgRoleGuid}                           |
| OrgRoleApi.Delete                     | /orgs/{orgGuid}/roles/{orgRoleGuid}                           |
| OrgRoleMembershipApi.Post             | /orgs/{orgGuid}/roles/{orgRoleGuid}/memberships               |
| OrgRoleMembershipApi.GetByOrgRoleGuid | /orgs/{orgGuid}/roles/{orgRoleGuid}/memberships               |
| OrgRoleMembershipApi.Delete           | /orgs/{orgGuid}/roles/{orgRoleGuid}/memberships/{accountGuid} |
| TenantApi.Post                        | /tenants                                                      |
| TenantApi.GetByDomain                 | /tenants?domain={domain}                                      |
| TenantApi.Get                         | /tenants/{orgGuid}                                            |
| TenantApi.Patch                       | /tenants/{orgGuid}                                            |
| TenantApi.Delete                      | /tenants/{orgGuid}                                            |
| TenantDomainApi.Post                  | /tenants/{orgGuid}/domains                                    |
| TenantDomainApi.Delete                | /tenants/{orgGuid}/domains/{domain}                           |
| UserApi.Post                          | /users                                                        |
| UserApi.GetByEmailAddress             | /users?emailAddress={emailAddress}                            |
| UserApi.Get                           | /users/{userGuid}                                             |
| UserApi.Patch                         | /users/{userGuid}                                             |
| UserApi.Delete                        | /users/{userGuid}                                             |
| UserRoleApi.Put                       | /users/{userGuid}/roles/{role}                                |
| UserRoleApi.Delete                    | /users/{userGuid}/roles/{userRoleGuid}                        |

## Soft Deleted Entities

This is a list of soft deleted entities, kept here for reasons of data retention.
Entities in this list should be hard-deleted after some retention period.

* `forms.form_instance`
* `forms.form_template`
* `orgs.feature`
* `orgs.org`
* `orgs.org_role`
* `users.account`
