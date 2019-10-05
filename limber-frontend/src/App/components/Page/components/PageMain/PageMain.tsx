import React, { CSSProperties, ReactNode } from 'react';

interface Props {
  children: ReactNode
}

const PageMain: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    flexGrow: 1,
    padding: '16px',
  };

  return <main style={style}>{props.children}</main>;
};

export default PageMain;