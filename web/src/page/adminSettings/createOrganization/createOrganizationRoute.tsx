import React from 'react';
import route from 'routing/route';

const CreateOrganizationPage = React.lazy(() => import('./CreateOrganizationPage'));

export default route(CreateOrganizationPage, { path: 'new' });
