import React, { CSSProperties } from 'react';
import { connect } from 'react-redux';
import State from '../../../../../state';
import { Link } from 'react-router-dom';

interface Props {
  color: string;
}

const HomePageFloater: React.FC<Props> = (props: Props) => {
  const containerStyle: CSSProperties = {
    alignSelf: 'center',
    flexGrow: 1,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  };

  const style: CSSProperties = {
    padding: '16px',
    backgroundColor: props.color,
  };

  const innerStyle: CSSProperties = {
    padding: '16px',
    backgroundColor: 'white',
  };

  return (
    <div style={containerStyle}>
      <div style={style}>
        <h1 style={{ color: 'white', textAlign: 'center' }}>Limber</h1>
        <div style={innerStyle}>
          <p style={{ textAlign: 'center' }}>
            Welcome to Limber. We don't have a marketing site yet.
            Click <Link to="/signin">here</Link> to sign in.
          </p>
        </div>
      </div>
    </div>
  );
};

export default connect((state: State) => ({ color: state.theme.navBarColor }))(HomePageFloater);
