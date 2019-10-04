import { FunctionalComponent, h } from 'preact';
import HeaderLink from './components/HeaderLink/HeaderLink';
import HeaderLinkGroup from './components/HeaderLinkGroup/HeaderLinkGroup';

const AppPageHeader: FunctionalComponent = () => {
  const style = {
    display: 'flex',
    height: '32px',
    backgroundColor: '#24292e',
    padding: '16px',
  };

  return <div style={style}>
    <HeaderLinkGroup>
      <HeaderLink to="/events">Limber</HeaderLink>
    </HeaderLinkGroup>
    <HeaderLinkGroup>
      <HeaderLink to="/events">Events</HeaderLink>
    </HeaderLinkGroup>
  </div>;
};

export default AppPageHeader;
