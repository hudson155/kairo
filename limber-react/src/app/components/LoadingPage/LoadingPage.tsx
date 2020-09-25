import React, { ReactElement } from 'react';

interface Props {
  readonly message: string;
}

function LoadingPage(props: Props): ReactElement {
  return <p>{props.message}</p>;
}

export default LoadingPage;
