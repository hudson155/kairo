/**
 * A type of error whose message should be surfaced to the user.
 * Generally, this is due to some incorrect or prohibited user action.
 */
export default class UserError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'UserError';
  }
}
