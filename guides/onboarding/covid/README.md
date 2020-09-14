# COVID-19 Onboarding

This README contains instructions for how to set up a client for COVID-19.
If you're following this guide, please keep it up to date by making a PR if anything is out of date.

## Prerequisites

- This guide assumes that [you have already onboarded the org](..).

## Steps

- [ ] [Create the feature and form templates](#create-the-feature-and-form-templates)

### Create the feature and form templates

Set the variables in
[this script](/limber-backend/monolith/src/main/kotlin/io/limberapp/backend/adhoc/CreateCovidFeature.kt)
and run `LIMBER_CONFIG=prod LIMBER_TASK=createCovidFeature ./gradlew limber-backend:monolith:run`.
