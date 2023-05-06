import ErrorBanner from 'component/error/ErrorBanner';
import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import Section from 'component/section/Section';
import LoadingBlock from 'component/spinner/LoadingBlock';
import OrganizationList from 'page/adminSettings/organizationList/OrganizationList';
import React, { ReactNode } from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import LocalStateContext from 'state/local/LocalStateContext';
import { useOrganizations } from 'state/local/organization/OrganizationProvider';

const OrganizationListPage: React.FC = () => {
  const organizations = useOrganizations();

  return (
    <Page>
      <HeaderSection title="Organization management" />
      <Section>
        {getContent(organizations)}
      </Section>
    </Page>
  );
};

export default OrganizationListPage;

const getContent = (organizations: LocalStateContext<Map<string, OrganizationRep>>): ReactNode => {
  switch (organizations.state) {
  case 'loading':
    return <LoadingBlock />;
  case 'hasError':
    return <ErrorBanner error={organizations.contents} operation="loading organizations" />;
  case 'hasValue': {
    return <OrganizationList organizations={[...organizations.contents.values()]} />;
  }
  }
};
