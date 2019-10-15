import React, { CSSProperties } from 'react';

const Loading: React.FC = () => {
  const style: CSSProperties = {
    alignSelf: 'center',
    flexGrow: 1,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  };

  return <div style={style}><p>Loading...</p></div>;
};

export default Loading;
