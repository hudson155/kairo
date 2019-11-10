import React from 'react';
import { Route, Switch } from 'react-router-dom';
import MarketingSiteHomePage from './pages/MarketingSiteHomePage/MarketingSiteHomePage';
import MarketingSiteNotFoundPage from './pages/MarketingSiteNotFoundPage/MarketingSiteNotFoundPage';
import Page from '../../components/Page/Page';

const MarketingSite: React.FC = () => {
  return (
    <Page>
      <Switch>
        <Route path="/" exact component={MarketingSiteHomePage} />
        <Route component={MarketingSiteNotFoundPage} />
      </Switch>
    </Page>
  );
};

export default MarketingSite;
