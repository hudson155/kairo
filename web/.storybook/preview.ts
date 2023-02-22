/* IMPORTANT: Preserve CSS import order. */
import 'the-new-css-reset/css/reset.css';
import 'index.scss';
import './storybook.scss';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Za-z0-9].*' },
};
