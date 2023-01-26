export default class ValidationErrorsError extends Error {
  errors: ValidationError[];

  constructor(errors: ValidationError[]) {
    super('Validation errors.');
    this.name = 'ValidationErrorsError';
    this.errors = errors;
  }
}

export interface ValidationError {
  propertyPath: string;
  message: string;
}