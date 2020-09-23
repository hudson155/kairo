INSERT INTO forms.form_instance (guid, created_date, feature_guid, form_template_guid,
                                 number, submitted_date, creator_account_guid)
VALUES (:guid, :createdDate, :featureGuid, :formTemplateGuid,
        :number, :submittedDate, :creatorAccountGuid)
RETURNING *
