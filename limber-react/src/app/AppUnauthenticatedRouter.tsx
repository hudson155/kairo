import React, { ReactElement } from 'react';
import { Link } from 'react-router-dom';

interface Props {
  readonly name: string;
}

function AppUnuthenticatedRouter(props: Props): ReactElement {
  return (
    <>
      <h1>Welcome to {props.name}</h1>
      <p>Powered by Limber</p>
      <Link to="/signin">Click here to sign in.</Link>
    </>
  );
}

export default AppUnuthenticatedRouter;
