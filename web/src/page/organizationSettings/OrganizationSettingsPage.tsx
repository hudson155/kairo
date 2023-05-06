import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import BasicInformation from 'page/organizationSettings/basicInformation/BasicInformation';
import React from 'react';
import { useRecoilValue } from 'recoil';
import organizationState from 'state/core/organization';

const OrganizationSettingsPage: React.FC = () => {
  const organization = useRecoilValue(organizationState);

  return (
    <Page>
      <HeaderSection title="Organization settings">
        <Paragraph>
          {'Manage settings for '}<Text weight="bold">{organization.name}</Text>
        </Paragraph>
      </HeaderSection>
      <BasicInformation organization={organization} />
    </Page>
  );
};

export default OrganizationSettingsPage;
