UPDATE forms.form_instance
SET submitted_date = COALESCE(:submittedDate, submitted_date)
WHERE guid = :guid
  AND archived_date IS NULL
