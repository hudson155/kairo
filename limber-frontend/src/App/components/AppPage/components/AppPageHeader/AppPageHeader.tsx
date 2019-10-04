import { FunctionalComponent, h } from 'preact';
import HeaderLink from './components/HeaderLink/HeaderLink';

const AppPageHeader: FunctionalComponent = () => {
  const style = {
    display: 'flex',
    height: '32px',
    backgroundColor: '#24292e',
    padding: '16px',
  };

  return <div style={style}>
    <HeaderLink to="/">Home</HeaderLink>
    <HeaderLink to="/events">Events</HeaderLink>
  </div>;
};

export default AppPageHeader;
