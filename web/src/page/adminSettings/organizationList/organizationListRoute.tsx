import React from 'react';
import route from 'routing/route';

const OrganizationListPage = React.lazy(() => import('./OrganizationListPage'));

export default route(OrganizationListPage, { path: '' });
