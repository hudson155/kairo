import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import Section from 'component/section/Section';
import OrganizationList from 'page/adminSettings/organizationList/OrganizationList';
import React from 'react';

const OrganizationListPage: React.FC = () => {
  return (
    <Page>
      <HeaderSection title="Organization management" />
      <Section>
        <OrganizationList />
      </Section>
    </Page>
  );
};

export default OrganizationListPage;
