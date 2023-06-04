import createOrganizationRoute from 'page/adminSettings/createOrganization/createOrganizationRoute';
import editOrganizationRoute from 'page/adminSettings/editOrganization/editOrganizationRoute';
import organizationListRoute from 'page/adminSettings/organizationList/organizationListRoute';
import React from 'react';
import { Routes } from 'react-router-dom';

const AdminOrganizationSettingsPage: React.FC = () => {
  return (
    <Routes>
      {organizationListRoute.route}
      {createOrganizationRoute.route}
      {editOrganizationRoute.route}
    </Routes>
  );
};

export default AdminOrganizationSettingsPage;
