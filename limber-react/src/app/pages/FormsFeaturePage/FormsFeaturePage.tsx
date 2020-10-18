import React, { ReactElement } from 'react';
import { Route, Switch, useRouteMatch } from 'react-router-dom';

import StandardLayout from '../../components/StandardLayout';

import FormInstanceViewPage, { formInstanceViewPageSubpath } from './FormInstanceViewPage/FormInstanceViewPage';

function FormsFeaturePage(): ReactElement {
  const match = useRouteMatch();

  return (
    <StandardLayout>
      <Switch>
        <Route path={`${match.path}/${formInstanceViewPageSubpath()}`}>
          <FormInstanceViewPage />
        </Route>
      </Switch>
    </StandardLayout>
  );
}

export default FormsFeaturePage;
