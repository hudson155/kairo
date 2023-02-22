import classNames from 'classnames';
import React, { ReactNode } from 'react';
import styles from './Container.module.scss';

type Density = 'very-compact' | 'compact' | 'normal' | 'relaxed';

type Direction = 'horizontal' | 'vertical';

interface Props {
  density?: Density;
  direction: Direction;
  children: ReactNode;
}

/**
 * The standard spacing/layout container.
 */
const Container: React.FC<Props> = ({ density = 'normal', direction, children }) => {
  return (
    <div className={classNames(styles.container, densityClassName(density), directionClassName(direction))}>
      {children}
    </div>
  );
};

export default Container;

const densityClassName = (density: Density): string => {
  switch (density) {
  case 'very-compact':
    return styles.densityVeryCompact;
  case 'compact':
    return styles.densityCompact;
  case 'normal':
    return styles.densityNormal;
  case 'relaxed':
    return styles.densityRelaxed;
  default:
    throw new Error(`Unsupported density: ${density}.`);
  }
};

const directionClassName = (direction: Direction): string => {
  switch (direction) {
  case 'horizontal':
    return styles.directionHorizontal;
  case 'vertical':
    return styles.directionVertical;
  default:
    throw new Error(`Unsupported direction: ${direction}.`);
  }
};
