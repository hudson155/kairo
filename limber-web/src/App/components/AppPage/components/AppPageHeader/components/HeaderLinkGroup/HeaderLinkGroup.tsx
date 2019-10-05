import React, { CSSProperties, ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const HeaderLinkGroup: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: '32px',
  };

  return <div style={style}>{props.children}</div>;
};

export default HeaderLinkGroup;
