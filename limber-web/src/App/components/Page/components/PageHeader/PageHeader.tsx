import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const PageHeader: React.FC<Props> = (props: Props) => {
  return <header>{props.children}</header>;
};

export default PageHeader;
