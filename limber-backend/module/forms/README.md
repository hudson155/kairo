# Forms Module

The forms module handles any form features an org has enabled.
An org can have 0 form features, or many form features,
although it's probably most common for an org to have 1 form feature.
A forms feature manifests itself as a distinct section within the app.
There's no communication between forms features, even within a single org.

So what is a forms feature?
A forms feature allows users to create form templates dynamically,
and instantiate or "fill out" those templates.
A form template akin to a real world blank form (or an infinite stack of blank forms) that are all identical.
A form instance is akin to a real world single form that has been filled out.
It pertains to a version of a form template, and the form template still exists.

Form templates are made up of questions, which are the atomic unit.
