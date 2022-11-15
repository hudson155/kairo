import { Menu as Delegate } from '@headlessui/react';
import React, { Fragment, ReactNode } from 'react';
import styles from './MenuItem.module.scss';

interface Props {
  children: (propArg: { className: string }) => ReactNode;
}

const MenuItem: React.FC<Props> = ({ children }) => {
  return (
    <Delegate.Item as={Fragment}>
      {children({ className: styles.item })}
    </Delegate.Item>
  );
};

export default MenuItem;
