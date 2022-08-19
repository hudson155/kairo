# Limber Application Platform

A platform that leverages shared code and shared infrastructure
to enable the development and hosting of multiple applications
with minimal duplication of effort.

The Limber Application Platform enables **high application repeatability**\
**without sacrificing customizability**. This **saves time and money**
while **accelerating project timelines** and **increasing application reliability**.

## Architecture

### Features

Limber' s modularity stems from its concept of Features.
An Application is composed from a set of Features,
each of which enables some specific functionality.

- Highly repeatable Features.
  - Dynamic forms:
    (Google Forms, JotForm, InUnison events).
  - Booking system
    (appointment booking, aircraft booking).
  - Process tracking for
    (manufacturing, sales).
- Derived or custom Application-specific Features.
- Features powered by third parties.
  - LMS-backed training
    (onboarding, compliance or certification training, aviation ground school).

By composing applications from Features,
reusability and configurability are inherent.

### Servers

All Features can run on a single shared Server to start,
which keeps the Infrastructure side very simple.
In development and with low-medium traffic volume,
you get the simplicity and cost savings of a monolithic architecture.

As the number of Servers grows and usage patterns diversify,
you can move towards more advanced architectures
where Features are free to scale independently by being offloaded to Feature-specific Servers.
Individual Applications can even have their own dedicated Servers as necessary.
Because Features are developed independently from the outset,
this architectural evolution is very easy.

### Applications

Applications share the underlying infrastructure
and can share Features where functionality similarities exist,
but run on their own domains,
potentially with their own UI themes.

- Authentication is delegated to Auth0.
  Each Limber application has a matching Auth0 organization,
  enabling maximum authentication flexibility.
  JWTs and user tokens are supported.
- Authorization is managed using feature-specific permission schemas.

### Infrastructure

Infrastructure is natively decoupled from Applications.
A single Server may serve requests for multiple Applications and multiple Features).
This keeps cost down.
Autoscaling is managed automatically by Kubernetes.
Each Server can scale independently, including Application-specific Servers.

Limber works with multiple datastores.

### Benefits

- Spin up a new Application without worrying about
  authentication/authorization, infrastructure,
  domain management and TLS certificates, or environment configuration.
- Manage infrastructure updates and version upgrades for all Applications just once.
  Reuse Features where possible between Applications.

### Challenges

- Facilitating Application transition from managed to standalone.

### Future

Possibility of eventually (many years down the line)
evolving into an aggregator for 3rd-party Limber Features,
essentially becoming what Squarespace is for websites
or what Shopify Ecommerce Plugins is for ecommerce,
but for custom software.

## Style guide

Other than the rules defined here, please follow the
[Google Style Guide](https://developers.google.com/style).

- **Sentence case:**
  Use American English style for general capitalization.
  Use sentence case in all headings, titles, and navigation.
  This includes for user-facing copy and within code and documentation.
  [This is consistent with the Google Style Guide](https://developers.google.com/style/text-formatting).
- **Product terminology:**
  The words [Feature](#features), [Server](#servers), and [Application](#applications)
  should be capitalized when they refer to the Limber-specific definitions of those terms.

## Chores

- Update all versions according to the [versions](#versions) section.
- Delete stale GitHub branches.
- Ensure Terraform's [API list](/infrastructure/terraform/apis.tf) is up-to-date.

## Versions

- Terraform `hashicorp/google` provider:
  [main.tf](/infrastructure/terraform/main.tf).
