import React from 'react';
import route from 'routing/route';

const AdminSettingsPage = React.lazy(() => import('./AdminSettingsPage'));

export default route(AdminSettingsPage, { path: 'admin', nesting: true });
