import { FunctionalComponent, h } from 'preact';

const AppPageFooter: FunctionalComponent = () => {
  const style = {
    display: 'flex',
    height: '32px',
    padding: '32px 16px',
  };

  return <div style={style}>
    <p>Copyright &copy; 2019 Jeff Hudson</p>
  </div>;
};

export default AppPageFooter;
