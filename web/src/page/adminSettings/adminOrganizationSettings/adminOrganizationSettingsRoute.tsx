import React from 'react';
import route from 'routing/route';

const AdminOrganizationSettingsPage = React.lazy(() => import('./AdminOrganizationSettingsPage'));

export default route(AdminOrganizationSettingsPage, { path: 'organizations', nesting: true });
