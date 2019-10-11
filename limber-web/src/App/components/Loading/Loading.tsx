import React, { CSSProperties } from 'react';

const Loading: React.FC = () => {
  const containerStyle: CSSProperties = {
    alignSelf: 'center',
    flexGrow: 1,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
  };

  return <div style={containerStyle}><p>Loading...</p></div>;
};

export default Loading;
