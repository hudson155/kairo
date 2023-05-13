import Container from 'component/container/Container';
import ErrorBanner from 'component/error/ErrorBanner';
import Paper from 'component/paper/Paper';
import Section from 'component/section/Section';
import LoadingBlock from 'component/spinner/LoadingBlock';
import Heading2 from 'component/text/Heading2';
import CreateAuthInformation from 'page/adminSettings/editOrganization/authInformation/CreateAuthInformation';
import UpdateAuthInformation from 'page/adminSettings/editOrganization/authInformation/UpdateAuthInformation';
import React, { ReactNode } from 'react';
import { Loadable, useRecoilValueLoadable } from 'recoil';
import OrganizationAuthRep from 'rep/OrganizationAuthRep';
import OrganizationRep from 'rep/OrganizationRep';
import organizationAuthsState from 'state/core/organizationAuthsState';

interface Props {
  organization: OrganizationRep;
}

const AuthInformation: React.FC<Props> = ({ organization }) => {
  const auth = useRecoilValueLoadable(organizationAuthsState(organization.guid));

  return (
    <Section>
      <Paper>
        <Container direction="vertical">
          <Heading2>{'Auth information'}</Heading2>
          {getContent(organization, auth)}
        </Container>
      </Paper>
    </Section>
  );
};

export default AuthInformation;

const getContent = (
  organization: OrganizationRep,
  auth: Loadable<OrganizationAuthRep | undefined>,
): ReactNode => {
  switch (auth.state) {
  case 'loading':
    return <LoadingBlock />;
  case 'hasError':
    return <ErrorBanner error={auth.contents} operation="loading organization auth" />;
  case 'hasValue': {
    if (!auth.contents) return <CreateAuthInformation organization={organization} />;
    return <UpdateAuthInformation auth={auth.contents} />;
  }
  }
};
