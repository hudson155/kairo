import FormField from 'component/form/FormField';

export default class FormFields extends Map<string, FormField> {
  getString(key: string): FormField {
    const field = this.get(key);
    if (!field) throw new Error(`Missing form field for ${key}.`);
    return field;
  }
}
