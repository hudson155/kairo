import React from 'react';
import HeaderLink from '../../../../components/Navbar/components/HeaderLink/HeaderLink';
import HeaderLinkGroup from '../../../../components/Navbar/components/HeaderLinkGroup/HeaderLinkGroup';
import Navbar from '../../../../components/Navbar/Navbar';

const MarketingSiteNavbar: React.FC = () => {
  return <Navbar
    left={<HeaderLinkGroup>
      <HeaderLink to="/">Limber</HeaderLink>
    </HeaderLinkGroup>}
    right={<HeaderLinkGroup>
      <HeaderLink to="/signin">Sign In / Sign Up</HeaderLink>
    </HeaderLinkGroup>} />;
};

export default MarketingSiteNavbar;
