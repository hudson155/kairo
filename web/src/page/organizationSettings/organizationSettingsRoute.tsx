import React from 'react';
import route from 'routing/route';

const OrganizationSettingsPage = React.lazy(() => import('./OrganizationSettingsPage'));

const organizationSettingsRoute = route(OrganizationSettingsPage, { path: 'settings', nesting: true });

export default organizationSettingsRoute;
