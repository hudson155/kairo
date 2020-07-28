UPDATE forms.form_instance_question
SET text       = COALESCE(:text, text),
    date       = COALESCE(:date, date),
    selections = COALESCE(:selections, selections)
WHERE form_instance_guid = :formInstanceGuid
  AND question_guid = :questionGuid
