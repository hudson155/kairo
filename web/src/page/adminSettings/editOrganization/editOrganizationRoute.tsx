import React from 'react';
import { useParams } from 'react-router-dom';
import route from 'routing/route';

const EditOrganizationPage = React.lazy(() => import('./EditOrganizationPage'));

const ORGANIZATION_GUID_PARAM = 'organizationGuid';

export default route(() => {
  const params = useParams();
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  return <EditOrganizationPage organizationGuid={params[ORGANIZATION_GUID_PARAM]!} />;
}, { path: `/:${ORGANIZATION_GUID_PARAM}` });
