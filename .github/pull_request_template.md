Resolves #0
or
Part of #0

### Description of changes

TODO

### Screenshots

(none)

### General

- [ ] Documentation (KDoc) exists but is not excessive or redundant.

### Endpoints

- [ ] Each endpoint is in a package that resembles its HTTP path.
- [ ] Each endpoint is documented using KDoc above the class.
- [ ] Each endpoint has appropriate authorization checks.

### DB and Stores

- [ ] Unique constraints and indices are considered and created.
- [ ] Unique constraint and index violations are handled.

### React components

- [ ] Each component is inside its own file, inside its own folder, nested within the appropriate parent's `/components` folder or `/pages` folder.
- [ ] Each component is documented using KDoc above the builder function.
- [ ] Each component's file is in this order: builder function (internal), props (internal), prop enums (internal), page object (internal), styles class and styles instantiation (private), private state, and then component (internal).
- [ ] Components don't use an excessive component class (global state, API).
- [ ] Styles have their own class (not object) with a unique name.
- [ ] Components are not too big.
- [ ] Components don't duplicate code from another component, and are sufficiently but not overly generic.
- [ ] Loading states, error states, and empty states are appropriately considered.
