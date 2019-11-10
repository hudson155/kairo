import React from 'react';

interface Props {
  children?: string;
}

const Loading: React.FC<Props> = (props: Props) => {
  return <p>Loading...{props.children && ` ${props.children}`}</p>;
};

export default Loading;
