import { TransitionClasses } from '@headlessui/react';
import classNames from 'classnames';
import styles from 'style/transitions.module.scss';

type TransitionType = 'fade' | 'moveTop' | 'slideLeft' | 'zoom';

/**
 * Returns properties matching Headless UI's expectations for transitions.
 */
export const transitions = (...types: TransitionType[]): TransitionClasses => {
  const transitionClassNames = types.map((transition) => styles[transition]);

  return {
    enter: styles.transition,
    enterFrom: classNames(transitionClassNames, styles.off),
    enterTo: classNames(transitionClassNames, styles.on),
    leave: styles.transition,
    leaveFrom: classNames(transitionClassNames, styles.on),
    leaveTo: classNames(transitionClassNames, styles.off),
  };
};
