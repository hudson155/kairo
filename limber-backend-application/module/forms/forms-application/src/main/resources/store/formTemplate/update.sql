UPDATE forms.form_template
SET title       = COALESCE(:title, title),
    description = COALESCE(:description, description)
WHERE guid = :guid
  AND archived_date IS NULL
