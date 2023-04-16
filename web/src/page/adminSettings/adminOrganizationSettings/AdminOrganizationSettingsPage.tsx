import organizationListRoute from 'page/adminSettings/organizationList/organizationListRoute';
import React from 'react';
import { Routes } from 'react-router-dom';

const AdminOrganizationSettingsPage: React.FC = () => {
  return (
    <Routes>
      {organizationListRoute.route}
    </Routes>
  );
};

export default AdminOrganizationSettingsPage;
