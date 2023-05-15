# Form Feature

The form feature supports customizable and fillable forms.

## Form templates

A **form template** is a custom form that can be filled in multiple times.
Think of it like a _blank_ PDF form.

- The **current** version of a template is the **live** version if there is one.
  If there isn't one, it's the **draft** version, which should be the only version.
  The current version of a template is the version that users will fill out.
- The **latest version is the most recent version.
  It may be **live** or **draft**.
  The latest version of a template is the version that feature admins will edit
  and eventually publish to become the current version.

Each version of a form template is stored in a separate row in the database.
The questions are stored as JSONB.

## Form instances

A **form instance** is an instance of a form template that has been filled out.
