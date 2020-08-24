UPDATE forms.form_instance_question
SET text       = COALESCE(:text, text),
    date       = COALESCE(:date, date),
    selections = COALESCE(:selections, selections)
WHERE EXISTS(SELECT 1 FROM forms.form_instance WHERE feature_guid = :featureGuid AND guid = form_instance_guid)
  AND form_instance_guid = :formInstanceGuid
  AND question_guid = :questionGuid
