# Onboarding

This README contains instructions for how to set up a brand new client on Limber.
If you're following this guide, please keep it up to date by making a PR if anything is out of date.

## Prerequisites

- The domain for the tenant.
    This can be a subdomain of `limberapp.io` (in which case we'll manage the DNS),
    or it can be their own custom domain.

## Steps

The steps below can be done in parallel, but keep 1 thing in mind:
**Don't visit the tenant domain until all steps are complete and you've waited 10 minutes.**
Browser and operating system DNS and TLS certificate caching is annoying. 

- [ ] [Set up a DNS record](#set-up-a-dns-record)
- [ ] [Add the Kubernetes configuration](#add-the-kubernetes-configuration)
- [ ] [Set up the Auth0 tenant](#set-up-the-auth0-tenant)
- [ ] [Manually insert database entities](#manually-insert-database-entities)
- [ ] Wait 10 minutes (seriously).
- [ ] [Test the tenant](#test-the-tenant)
- [ ] [Update the owner account GUID](#update-the-owner-account-guid)

### Set up a DNS record

1. Create a DNS A record for the host, with a TTL of 1 hour and a value of `104.248.105.23`.
    1. For `limberapp.io` subdomains, our domain is managed by Jeff's personal Google Domains account,
        so he will need to do this for you.

### Add the Kubernetes configuration

We need to modify the Kubernetes config to support a new client.
We also need to modify the Kubernetes config in the future if their domain changes.

1. Update `infrastructure/kubernetes/limber/ingress.yaml` to include a TLS config _and_ a rule for the host.

2. Run `kubectl apply --recursive -f infrastructure/kubernetes/limber --prune -l instance=limber --namespace limber`
    to apply the infrastructure changes.

3. Wait until the
    [Kubernetes dashboard](https://cloud.digitalocean.com/kubernetes/clusters/f008d8aa-d8da-4ccd-8266-e0808029709b/db/c5c479b0-a8ed-4704-9791-c41ad6470f87/#/overview?namespace=_all)
    goes all-green again.

### Set up the Auth0 tenant

Auth0 manages authentication for all tenants.
We need to set up a tenant in Auth0 that is specific to the client.
The Auth0 tenant will hit the same Auth0 domain and possibly use some of the same Auth0 connections
as other Auth0 tenants.

1. Navigate to https://manage.auth0.com/dashboard/us/limber/applications.

2. Click "Create Application".
    - Provide the organization name as the "Name".
        Names should be unique though, so if a client has or might have multiple tenants, disambiguate somehow.
    - Choose "Single Page Web Applications".
    - Hit "Create".

3. Configure things on the "Settings" page. In this example, we're using the term "placeholder" in a few places. Replace
    it as necessary.
    - Application Login URI: `https://placeholder.limberapp.io/signin`.
    - Allowed Callback URLs: `https://placeholder.limberapp.io`.
    - Allowed Logout URLs: `https://placeholder.limberapp.io`.
    - Allowed Web Origins: `https://placeholder.limberapp.io`.
    - Hit "Save Changes".

4. Manage desired connections on the "Connections" page.

### Manually insert database entities

This inserts the most basic database entities.
Make these requests using Postman, and use a JWT from a user that has global admin status.

1. **Create the org**. Note, you'll change the `ownerAccountGuid` later.
    ```
    POST https://api.limberapp.io/orgs
    {
        "name": "Placeholder",
        "ownerAccountGuid": "00000000-0000-0000-0000-000000000000"
    }
    ```

1. **Create the tenant**.
    ```
    POST https://api.limberapp.io/tenants
    {
    	"orgGuid": "<placeholder>", // Get this from the "Create the org" request's response.
    	"auth0ClientId": "<placeholder>", // Get this from Auth0.
    	"domain": {
    		"domain": "placeholder.limberapp.io"
    	}
    }

### Test the tenant

**Did you wait 10 minutes???**

Navigate to https://placeholder.limberapp.io in your browser and sign in.
Make sure everything works ok.

### Update the owner account GUID

1. Manually update the `ownerAccountGuid` for the organization.
```postgresql
UPDATE orgs.org
SET owner_account_guid = :ownerAccountGuid
WHERE guid = :orgGuid
```
