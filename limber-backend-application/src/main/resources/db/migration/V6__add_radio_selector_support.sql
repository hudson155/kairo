ALTER TABLE forms.form_template_question
ADD COLUMN options TEXT[];

ALTER TABLE forms.form_instance_question
ADD COLUMN selections TEXT[];
