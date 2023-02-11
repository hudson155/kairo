import Paper from 'component/paper/Paper';
import HeaderSection from 'component/section/HeaderSection';
import Section from 'component/section/Section';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import BasicInformation from 'page/organizationSettings/basicInformation/BasicInformation';
import React from 'react';
import { useRecoilValue } from 'recoil';
import organizationNameState from 'state/core/organizationName';

const OrganizationSettingsPage: React.FC = () => {
  const organizationName = useRecoilValue(organizationNameState);

  return (
    <>
      <HeaderSection title="Organization settings">
        <Paragraph>
          {'Manage settings for '}<Text weight="bold">{organizationName}</Text>
        </Paragraph>
      </HeaderSection>
      <Section>
        <Paper>
          <BasicInformation />
        </Paper>
      </Section>
    </>
  );
};

export default OrganizationSettingsPage;
