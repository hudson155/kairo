import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import BasicInformation from 'page/adminSettings/createOrganization/basicInformation/BasicInformation';
import React from 'react';

const CreateOrganizationPage: React.FC = () => {
  return (
    <Page>
      <HeaderSection title="Create organization" />
      <BasicInformation />
    </Page>
  );
};

export default CreateOrganizationPage;
