import React, { CSSProperties } from 'react';

interface Props {
  children: string;
}

const HeaderText: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    marginRight: '16px',
    color: 'white',
  };

  return <div style={style}>{props.children}</div>;
};

export default HeaderText;
