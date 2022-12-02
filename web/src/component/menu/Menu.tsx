import { Menu as Delegate, Transition } from '@headlessui/react';
import classNames from 'classnames';
import React, { Fragment, ReactNode } from 'react';
import { transitions } from 'style/transitions';
import styles from './Menu.module.scss';

interface Props {
  button: ReactNode;
  side?: 'left' | 'right';
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
        <Delegate.Items as="div" className={classNames(styles.container, styles[side])}>{children}</Delegate.Items>
      </Transition>
    </Delegate>
  );
};

export default Menu;
