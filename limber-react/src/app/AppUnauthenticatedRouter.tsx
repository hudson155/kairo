import React, { ReactElement } from 'react';

interface Props {
  readonly name: string;
}

function AppUnuthenticatedRouter(props: Props): ReactElement {
  return (
    <>
      <h1>Welcome to {props.name}</h1>
      <p>Powered by Limber</p>
      <span>Please sign in to continue</span>
    </>
  );
}

export default AppUnuthenticatedRouter;
