import { Menu as Delegate } from '@headlessui/react';
import classNames from 'classnames';
import React, { Fragment, ReactElement } from 'react';
import styles from './MenuItem.module.scss';

interface Props {
  children: (propArg: { className: string }) => ReactElement;
}

const MenuItem: React.FC<Props> = ({ children }) => {
  return (
    <Delegate.Item as={Fragment}>
      {({ active }) => children({ className: classNames(styles.item, { focus: active }) })}
    </Delegate.Item>
  );
};

export default MenuItem;
