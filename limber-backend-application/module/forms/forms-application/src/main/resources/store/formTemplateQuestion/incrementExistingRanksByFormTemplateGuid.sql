UPDATE forms.form_template_question
SET rank = rank + :incrementBy
WHERE form_template_guid = :formTemplateGuid
  AND rank >= :atLeast
