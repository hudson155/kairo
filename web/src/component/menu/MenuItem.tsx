import { Menu as Delegate } from '@headlessui/react';
import classNames from 'classnames';
import styles from 'component/menu/MenuItem.module.scss';
import React, { Fragment, ReactElement } from 'react';

interface Props {
  children: (propArg: { className: string; close: () => void }) => ReactElement;
}

const MenuItem: React.FC<Props> = ({ children }) => {
  return (
    <Delegate.Item as={Fragment}>
      {({ active, close }) => children({ className: classNames(styles.item, { focus: active }), close })}
    </Delegate.Item>
  );
};

export default MenuItem;
