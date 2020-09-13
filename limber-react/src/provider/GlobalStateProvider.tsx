import React, { useEffect, useState } from 'react';
import { useApi } from './ApiProvider';
import { OrgRepComplete } from '../rep/Org';
import LoadingPage from '../app/components/LoadingPage/LoadingPage';
import OrgProvider from './OrgProvider';

interface Props {
  readonly orgGuid: string;
}

const GlobalStateProvider: React.FC<Props> = (props) => {
  const api = useApi();

  const [org, setOrg] = useState<OrgRepComplete>();

  useEffect(() => {
    (async () => setOrg(await api.getOrg(props.orgGuid)))();
  }, [props.orgGuid]);

  if (!org) return <LoadingPage message="Loading org..." />;

  return <OrgProvider value={org}>{props.children}</OrgProvider>;
};

export default GlobalStateProvider;
