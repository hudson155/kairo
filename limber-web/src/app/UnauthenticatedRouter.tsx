import React from 'react';
import { Route, Switch } from 'react-router-dom';

const UnauthenticatedRouter: React.FC = () => {
  return (
    <Switch>
      <Route>{/* TODO: Create a fallback page */}</Route>
    </Switch>
  );
};

export default UnauthenticatedRouter;
