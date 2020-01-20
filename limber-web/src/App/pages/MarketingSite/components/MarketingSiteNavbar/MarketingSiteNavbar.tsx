import React from 'react';
import Navbar from '../../../../components/Navbar/Navbar';
import HeaderLinkGroup from '../../../../components/Navbar/components/HeaderLinkGroup/HeaderLinkGroup';
import HeaderLink from '../../../../components/Navbar/components/HeaderLink/HeaderLink';

const MarketingSiteNavbar: React.FC = () => {
  return <Navbar
    left={<>
      <HeaderLinkGroup>
        <HeaderLink to="/">Limber</HeaderLink>
      </HeaderLinkGroup>
    </>}
    right={<>
      <HeaderLinkGroup>
        <HeaderLink to="/signin">Sign In / Sign Up</HeaderLink>
      </HeaderLinkGroup>
    </>} />;
};

export default MarketingSiteNavbar;
