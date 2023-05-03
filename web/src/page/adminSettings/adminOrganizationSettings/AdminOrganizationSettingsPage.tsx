import organizationListRoute from 'page/adminSettings/organizationList/organizationListRoute';
import React from 'react';
import { Routes } from 'react-router-dom';
import OrganizationsProvider from 'state/local/organization/OrganizationProvider';

const AdminOrganizationSettingsPage: React.FC = () => {
  return (
    <OrganizationsProvider>
      <Routes>
        {organizationListRoute.route}
      </Routes>
    </OrganizationsProvider>
  );
};

export default AdminOrganizationSettingsPage;
