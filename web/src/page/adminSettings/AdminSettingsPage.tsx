import adminOrganizationSettingsRoute from 'page/adminSettings/adminOrganizationSettings/adminOrganizationSettingsRoute';
import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';

const AdminSettingsPage: React.FC = () => {
  return (
    <Routes>
      <Route element={<Navigate replace={true} to={adminOrganizationSettingsRoute.path} />} path="/" />
      {adminOrganizationSettingsRoute.route}
    </Routes>
  );
};

export default AdminSettingsPage;
