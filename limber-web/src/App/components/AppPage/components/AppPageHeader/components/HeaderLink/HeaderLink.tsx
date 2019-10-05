import React, { CSSProperties } from 'react';
import { Link } from 'react-router-dom';

interface Props {
  to: string;
  children: string;
}

const HeaderLink: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    marginRight: '16px',
    color: 'white',
    fontWeight: 'bold',
    textDecoration: 'none',
  };

  return (
    <Link to={props.to} style={style}>
      {props.children}
    </Link>
  );
};

export default HeaderLink;
