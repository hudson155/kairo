UPDATE forms.form_instance
SET number         = COALESCE(number, CASE :setNumber
                                          WHEN TRUE THEN (SELECT COALESCE(MAX(number), 0) + 1
                                                          FROM forms.form_instance
                                                          WHERE feature_guid = :featureGuid) END),
    submitted_date = COALESCE(:submittedDate, submitted_date)
WHERE feature_guid = :featureGuid
  AND guid = :formInstanceGuid
RETURNING *
