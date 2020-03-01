import React, { CSSProperties } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import State from '../../../../../state';

interface Props {
  color: string;
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

  return <Link to={props.to} style={style}>{props.children}</Link>;
};

export default connect((state: State) => ({
  color: state.theme.model!!.navBarLinkColor,
}))(HeaderLink);
