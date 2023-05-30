import React from 'react';
import { useParams } from 'react-router-dom';
import route from 'routing/route';

const EditOrganizationPage = React.lazy(() => import('./EditOrganizationPage'));

const ORGANIZATION_ID_PARAM = 'organizationId';

export default route(() => {
  const params = useParams();
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  return <EditOrganizationPage organizationId={params[ORGANIZATION_ID_PARAM]!} />;
}, { path: `/:${ORGANIZATION_ID_PARAM}` });
