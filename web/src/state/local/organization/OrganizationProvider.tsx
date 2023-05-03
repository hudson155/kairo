import { organizationApiState } from 'api/OrganizationApi';
import React, { ReactNode, Suspense, useContext } from 'react';
import { useRecoilValue } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import LocalStateContext, { createLocalStateContext, useLocalStateContext } from 'state/local/LocalStateContext';

export const context = createLocalStateContext<Map<string, OrganizationRep>>();

interface Props {
  children: ReactNode;
}

const OrganizationsProvider: React.FC<Props> = ({ children }) => {
  const organizationApi = useRecoilValue(organizationApiState);

  const contents = useLocalStateContext(async () => {
    const organizations = await organizationApi.listAll();
    return new Map(organizations.map((organization) => [organization.guid, organization]));
  }, [organizationApi]);

  if (!contents) return null;

  return (
    <context.Provider value={contents}>
      <Suspense>{children}</Suspense>
    </context.Provider>
  );
};

export default OrganizationsProvider;

export const useOrganizations = (): LocalStateContext<Map<string, OrganizationRep>> => {
  return useContext(context);
};
