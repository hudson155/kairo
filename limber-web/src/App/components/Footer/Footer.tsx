import React, { CSSProperties } from 'react';

const Footer: React.FC = () => {
  const style: CSSProperties = {
    display: 'flex',
    padding: '16px 16px',
  };

  return <div style={style}>
    <p>
      <small>
        Copyright &copy; {new Date().getFullYear()} {process.env['REACT_APP_COPYRIGHT_HOLDER']}
      </small>
    </p>
  </div>;
};

export default Footer;
