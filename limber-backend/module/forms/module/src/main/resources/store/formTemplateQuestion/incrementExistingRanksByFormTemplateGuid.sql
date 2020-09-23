UPDATE forms.form_template_question
SET rank = rank + 1
WHERE form_template_guid = :formTemplateGuid
  AND rank >= :atLeast
