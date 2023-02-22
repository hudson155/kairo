import { Menu as Delegate, Transition } from '@headlessui/react';
import classNames from 'classnames';
import styles from 'component/menu/Menu.module.scss';
import React, { Fragment, ReactNode } from 'react';
import { transitions } from 'style/transitions';

type Side = 'left' | 'right';

interface Props {
  button: ReactNode;
  side?: Side;
  children: ReactNode;
}

/**
 * A navigation component approximating a "drop-down menu",
 * The open button is arbitrary, but the children should use [MenuItems] and [MenuItem].
 */
const Menu: React.FC<Props> = ({ button, side = 'left', children }) => {
  return (
    <Delegate as="div" className={styles.menu}>
      <Delegate.Button as={Fragment}>{button}</Delegate.Button>
      <Transition as={Fragment} {...transitions('fadeIn', 'moveDownIn', 'fadeOut', 'moveUpOut')}>
        <Delegate.Items as="div" className={classNames(styles.container, sideClassName(side))}>
          {children}
        </Delegate.Items>
      </Transition>
    </Delegate>
  );
};

export default Menu;

export const sideClassName = (side: Side): string => {
  switch (side) {
  case 'left':
    return styles.left;
  case 'right':
    return styles.right;
  default:
    throw new Error(`Unsupported side: ${side}.`);
  }
};
