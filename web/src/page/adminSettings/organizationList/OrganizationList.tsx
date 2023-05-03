import ErrorBanner from 'component/error/ErrorBanner';
import LoadingBlock from 'component/spinner/LoadingBlock';
import Paragraph from 'component/text/Paragraph';
import styles from 'page/adminSettings/organizationList/OrganizationList.module.scss';
import OrganizationListItem from 'page/adminSettings/organizationList/OrganizationListItem';
import React from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import { useOrganizations } from 'state/local/organization/OrganizationProvider';

const OrganizationList: React.FC = () => {
  const organizationsContext = useOrganizations();

  if (organizationsContext.state === 'loading') {
    return <LoadingBlock />;
  }

  if (organizationsContext.state === 'hasError') {
    return <ErrorBanner error={organizationsContext.contents} operation="loading organizations" />;
  }

  const organizations = [...organizationsContext.getValue().values()];

  if (organizations.length === 0) {
    return <Paragraph>{'No organizations found.'}</Paragraph>;
  }

  return (
    <div className={styles.list}>
      {organizationListItems(organizations)}
    </div>
  );
};

export default OrganizationList;

const organizationListItems = (organizations: OrganizationRep[]): React.ReactNode => {
  return organizations.map((organization) => {
    return <OrganizationListItem key={organization.guid} organization={organization} />;
  });
};
