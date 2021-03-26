export function checkNotUndefined<T>(value: T | undefined, errorMessage: string): T {
  if (value === undefined) throw new Error(errorMessage);
  return value;
}
