import NotFoundBanner from 'component/banner/NotFoundBanner';
import ErrorBanner from 'component/error/ErrorBanner';
import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import LoadingBlock from 'component/spinner/LoadingBlock';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import AuthInformation from 'page/adminSettings/editOrganization/authInformation/AuthInformation';
import DeleteOrganization from 'page/adminSettings/editOrganization/deleteOrganization/DeleteOrganization';
import BasicInformation from 'page/organizationSettings/basicInformation/BasicInformation';
import React, { ReactNode } from 'react';
import { Loadable, useRecoilValueLoadable } from 'recoil';
import OrganizationRep from 'rep/OrganizationRep';
import organizationsOrganizationState from 'state/admin/organizationsOrganization';

interface Props {
  organizationId: string;
}

const EditOrganizationPage: React.FC<Props> = ({ organizationId }) => {
  const organization = useRecoilValueLoadable(organizationsOrganizationState(organizationId));

  return (
    <Page>
      <HeaderSection title="Edit organization">
        {getSubtitle(organization)}
      </HeaderSection>
      {getContent(organization)}
    </Page>
  );
};

export default EditOrganizationPage;

const getSubtitle = (organization: Loadable<OrganizationRep | undefined>): ReactNode => {
  switch (organization.state) {
  case 'hasValue': {
    if (!organization.contents) return null;
    return (
      <Paragraph>
        {'Manage settings for '}<Text weight="bold">{organization.contents.name}</Text>
      </Paragraph>
    );
  }
  default:
    return null;
  }
};

const getContent = (organization: Loadable<OrganizationRep | undefined>): ReactNode => {
  switch (organization.state) {
  case 'loading':
    return <LoadingBlock />;
  case 'hasError':
    return <ErrorBanner error={organization.contents} operation="loading organizations" />;
  case 'hasValue': {
    if (!organization.contents) return <NotFoundBanner entity="organization" />;
    return (
      <>
        <BasicInformation organization={organization.contents} />
        <AuthInformation organization={organization.contents} />
        <DeleteOrganization organization={organization.contents} />
      </>
    );
  }
  }
};
