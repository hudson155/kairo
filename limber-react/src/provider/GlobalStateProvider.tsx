import React, { ReactElement, ReactNode, useEffect, useState } from 'react';

import LoadingPage from '../app/pages/LoadingPage/LoadingPage';
import { OrgRepComplete } from '../rep/Org';

import { useApi } from './ApiProvider';
import OrgProvider from './OrgProvider';

interface Props {
  readonly children: ReactNode;
  readonly orgGuid: string;
}

function GlobalStateProvider(props: Props): ReactElement {
  const api = useApi();

  const [org, setOrg] = useState<OrgRepComplete>();

  useEffect(() => {
    (async () => setOrg(await api.getOrg(props.orgGuid)))();
  }, [api, props.orgGuid]);

  if (!org) return <LoadingPage message="Loading org..." />;

  return <OrgProvider value={org}>{props.children}</OrgProvider>;
}

export default GlobalStateProvider;
