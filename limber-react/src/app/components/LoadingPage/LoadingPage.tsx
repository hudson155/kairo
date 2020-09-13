import React from 'react';

interface Props {
  readonly message: string;
}

const LoadingPage: React.FC<Props> = (props) => {
  return <p>{props.message}</p>;
};

export default LoadingPage;
