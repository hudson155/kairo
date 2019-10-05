import React, { CSSProperties } from 'react';

const AppPageFooter: React.FC = () => {
  const style: CSSProperties = {
    display: 'flex',
    height: '32px',
    padding: '32px 16px',
  };

  return (
    <div style={style}>
      <p>
        <small>Copyright &copy; 2019 Jeff Hudson</small>
      </p>
    </div>
  );
};

export default AppPageFooter;
