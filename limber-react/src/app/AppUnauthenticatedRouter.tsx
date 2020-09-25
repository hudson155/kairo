import React, { ReactElement } from 'react';
import { Link } from 'react-router-dom';

function AppUnuthenticatedRouter(): ReactElement {
  return (
    <>
      <h1>Welcome to Limber</h1>
      <Link to="/signin">Click here to sign in.</Link>
    </>
  );
}

export default AppUnuthenticatedRouter;
