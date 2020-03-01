import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Footer from '../../components/Footer/Footer';
import Page from '../../components/Page/Page';
import MarketingSiteNavbar from './components/MarketingSiteNavbar/MarketingSiteNavbar';
import MarketingSiteHomePage from './pages/MarketingSiteHomePage/MarketingSiteHomePage';
import MarketingSiteNotFoundPage from './pages/MarketingSiteNotFoundPage/MarketingSiteNotFoundPage';

const MarketingSite: React.FC = () => {
  return <Page header={<MarketingSiteNavbar />} footer={<Footer />}>
    <Switch>
      <Route path="/" exact component={MarketingSiteHomePage} />
      <Route component={MarketingSiteNotFoundPage} />
    </Switch>
  </Page>;
};

export default MarketingSite;
