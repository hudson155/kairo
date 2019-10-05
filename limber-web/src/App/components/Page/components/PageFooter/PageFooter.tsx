import React, { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const PageFooter: React.FC<Props> = (props: Props) => {
  return <footer>{props.children}</footer>;
};

export default PageFooter;
