# Organization Feature

As a multi-tenant application,
Limber uses the Organization Feature to store tenant-specific information.

This includes basic tenant metadata and the list of features for that tenant,
but should not include anything pertaining to feature implementations or users.

**A note about organization features:**
An organization's features are different from Limber features.
Limber Features are a building block in the Limber framework.
Organization features define a piece of functionality in the app.
However, organization features are backed by Limber Features.
