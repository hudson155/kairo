import React, { CSSProperties, ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const PageMainContainer: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    width: '1080px',
    maxWidth: '100%',
    alignSelf: 'center',
  };

  return <div style={style}>{props.children}</div>;
};

export default PageMainContainer;
