# Validation library

Limber uses [Hibernate Validator](https://hibernate.org/validator/) for validation.
_Note: This is unrelated to Hibernate's ORM product._

Instead of using Hibernate's annotations directly,
we wrap them using [constraint composition](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-constraint-composition).
One reason for this is to avoid needing to specify the annotations' use-site targets,
but it also provides a testable and clean validation interface.

**Do not use Hibernate's annotations directly.**

Validators that are common across the app live in this Gradle module,
but feature-specific validators live in the corresponding feature.
