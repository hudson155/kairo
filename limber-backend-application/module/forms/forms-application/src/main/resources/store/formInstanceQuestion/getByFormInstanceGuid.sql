SELECT form_instance_question.*
FROM forms.form_instance_question
         JOIN forms.form_template_question ON form_instance_question.question_guid = form_template_question.guid
WHERE form_instance_guid = :formInstanceGuid
ORDER BY rank
