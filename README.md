# Limber Application Platform

Limber leverages shared code and shared infrastructure
to enable the development and hosting of multiple Applications
with minimal duplication of effort.

The Limber Application Platform enables **high Application repeatability**
**without sacrificing customizability**. This **saves time and money**
while **accelerating project timelines** and **increasing Application reliability**.

## Architecture

### Features

Limber's modularity stems from its concept of Features.
An Application is composed of a set of Features,
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

By composing Applications from Features,
reusability and configurability are inherent.

### Servers

A Server runs a collection of Features.

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
  Each Limber Application has a matching Auth0 organization,
  enabling maximum authentication flexibility.
  JWTs and user tokens are supported.
- Authorization is managed using Feature-specific permission schemas.

### Infrastructure

Infrastructure is natively decoupled from Applications.
A single Server may serve requests for multiple Applications and multiple Features.
This keeps cost down.
Autoscaling is managed automatically by Google App Engine.
Each Server can scale independently, including Application-specific Servers.

Limber works with multiple datastores.

### Benefits

- Spin up a new Application without worrying about
  authentication/authorization, infrastructure,
  domain management and TLS certificates, or environment configuration.
- Manage infrastructure updates and version upgrades for all Applications just once.
  Reuse Features where possible between Applications.
