import React from 'react';
import { Route, Switch } from 'react-router-dom';
import MarketingSiteHomePage from './pages/MarketingSiteHomePage/MarketingSiteHomePage';

const MarketingSite: React.FC = () => {
  return (
    <Switch>
      <Route key="/" path="/" exact component={MarketingSiteHomePage} />
    </Switch>
  );
};

export default MarketingSite;
