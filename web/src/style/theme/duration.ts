import theme from 'style/theme/duration.module.scss';

/**
 * Changing CSS too quickly can cause transitions to be skipped.
 * For example CSS transitions don't work for transitions from [auto].
 * Changing a value from [auto] to a fixed number and then to another fixed number
 * can cause the transition to be skipped as if it were transitioning directly from [auto].
 * This delay prevents transitions from being skipped.
 */
export const durationCssDelay: number = 5;

export const durationFlash: number = Number(theme.durationFlash);

export const durationTransition: number = Number(theme.durationTransition);
