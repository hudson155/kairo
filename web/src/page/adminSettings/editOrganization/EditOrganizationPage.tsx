import NotFoundBanner from 'component/banner/NotFoundBanner';
import ErrorBanner from 'component/error/ErrorBanner';
import Page from 'component/page/Page';
import HeaderSection from 'component/section/HeaderSection';
import LoadingBlock from 'component/spinner/LoadingBlock';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import React, { ReactNode } from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import LocalStateContext from 'state/local/LocalStateContext';
import { useOrganizations } from 'state/local/organization/OrganizationProvider';

interface Props {
  organizationGuid: string;
}

const EditOrganizationPage: React.FC<Props> = ({ organizationGuid }) => {
  const organizations = useOrganizations();

  return (
    <Page>
      <HeaderSection title="Edit organization">
        {getSubtitle(organizations, organizationGuid)}
      </HeaderSection>
      {getContent(organizations, organizationGuid)}
    </Page>
  );
};

export default EditOrganizationPage;

const getSubtitle = (
  organizations: LocalStateContext<Map<string, OrganizationRep>>,
  organizationGuid: string,
): ReactNode => {
  switch (organizations.state) {
  case 'hasValue': {
    const organization = organizations.contents.get(organizationGuid);
    if (!organization) return null;
    return (
      <Paragraph>
        {'Manage settings for '}<Text weight="bold">{organization.name}</Text>
      </Paragraph>
    );
  }
  default:
    return null;
  }
};

const getContent = (
  organizations: LocalStateContext<Map<string, OrganizationRep>>,
  organizationGuid: string,
): ReactNode => {
  switch (organizations.state) {
  case 'loading':
    return <LoadingBlock />;
  case 'hasError':
    return <ErrorBanner error={organizations.contents} operation="loading organizations" />;
  case 'hasValue': {
    const organization = organizations.contents.get(organizationGuid);
    if (!organization) return <NotFoundBanner entity="organization" />;
    return null;
  }
  }
};
