import React from 'react';
import PageMainContainer from '../../../../components/PageMainContainer/PageMainContainer';
import { Link } from 'react-router-dom';

const MarketingSiteHomePage: React.FC = () => {
  return <PageMainContainer>
    <h1>Limber</h1>
    <p>
      Welcome to Limber. We don&apos;t have a marketing site yet.&nbsp;
      <Link to="/signin">Click here to sign in</Link>.
    </p>
  </PageMainContainer>;
};

export default MarketingSiteHomePage;
