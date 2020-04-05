ALTER TABLE forms.form_template_question
ADD COLUMN
    options                 TEXT[],
ADD COLUMN
    selection               TEXT[];
