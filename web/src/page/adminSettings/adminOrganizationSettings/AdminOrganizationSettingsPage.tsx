import editOrganizationRoute from 'page/adminSettings/editOrganization/editOrganizationRoute';
import organizationListRoute from 'page/adminSettings/organizationList/organizationListRoute';
import React from 'react';
import { Routes } from 'react-router-dom';
import OrganizationsProvider from 'state/local/organization/OrganizationProvider';

const AdminOrganizationSettingsPage: React.FC = () => {
  return (
    <OrganizationsProvider>
      <Routes>
        {organizationListRoute.route}
        {editOrganizationRoute.route}
      </Routes>
    </OrganizationsProvider>
  );
};

export default AdminOrganizationSettingsPage;
