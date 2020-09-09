# Onboarding

This README contains instructions for how to set up a brand new client on Limber.
If you're following this guide, please keep it up to date by making a PR if anything is out of date.

## Prerequisites

- The name of the tenant.
- The domain for the tenant.

## Steps

The steps below can be done in parallel, but keep 1 thing in mind:
**Don't visit the tenant domain until all steps are complete and you've waited 10 minutes.**
Browser and operating system DNS and TLS certificate caching is annoying.

- [ ] [Set up a DNS record](#set-up-a-dns-record)
- [ ] [Add the Kubernetes configuration](#add-the-kubernetes-configuration)
- [ ] [Set up the Auth0 tenant](#set-up-the-auth0-tenant)
- [ ] [Onboard the org](#onboard-the-org)
- [ ] Wait 10 minutes (seriously).
- [ ] [Test the tenant](#test-the-tenant)
- [ ] [Set the owner user GUID](#set-the-owner-user-guid)

### Set up a DNS record

1. Create a DNS A record for the host, with a TTL of 1 hour and a value of `174.138.114.191`.

### Add the Kubernetes configuration

We need to modify the Kubernetes config to support a new client.
We also need to modify the Kubernetes config in the future if their domain changes.

1. Update `infrastructure/kubernetes/limber/ingress.yaml` to include a TLS config _and_ a rule for the host.

2. Run `kubectl apply --recursive -f infrastructure/kubernetes/limber --prune -l instance=limber --namespace limber`
    to apply the infrastructure changes.

3. Wait until the
    [Kubernetes dashboard](https://cloud.digitalocean.com/kubernetes/clusters/9a0961f1-ad6b-4513-ab64-b7491fb5cc80/db/a69ec443cb9345ca353c8482c7651416f5a77826/#/overview?namespace=_all)
    goes all-green again.

### Set up the Auth0 tenant

Auth0 manages authentication for all tenants.
We need to set up a tenant in Auth0 that is specific to the client.
The Auth0 tenant will hit the same Auth0 domain and possibly use some of the same Auth0 connections
as other Auth0 tenants.

1. Navigate to https://manage.auth0.com/dashboard/us/limber/applications.

2. Click "Create Application".
    - Provide the domain name as the "Name".
    - Choose "Single Page Web Applications".
    - Hit "Create".

3. Configure things on the "Settings" page.
    - Application Login URI: `https://{domain}/signin`.
    - Allowed Callback URLs: `https://{domain}`.
    - Allowed Logout URLs: `https://{domain}`.
    - Allowed Web Origins: `https://{domain}`.
    - Hit "Save Changes".

4. Manage desired connections on the "Connections" page.

### Onboard the org

Set the variables in
[this script](/limber-backend-application/src/main/kotlin/io/limberapp/backend/adhoc/Onboard.kt)
and run `LIMBER_CONFIG=prod LIMBER_TASK=onboard ./gradlew limber-backend-application:run`.

### Test the tenant

**Did you wait 10 minutes???**

Navigate to https://placeholder.limberapp.io in your browser and sign in.
Make sure everything works ok.

### Set the owner user GUID

Set the variables in
[this script](/limber-backend-application/src/main/kotlin/io/limberapp/backend/adhoc/UpdateOwnerUserGuid.kt)
and run `LIMBER_CONFIG=prod LIMBER_TASK=updateOwnerUserGuid ./gradlew limber-backend-application:run`.
