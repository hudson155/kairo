import { Menu as Delegate } from '@headlessui/react';
import React, { Fragment, ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const MenuItem: React.FC<Props> = ({ children }) => {
  return <Delegate.Item as={Fragment}>{children}</Delegate.Item>;
};

export default MenuItem;
