import Paragraph from 'component/text/Paragraph';
import styles from 'page/adminSettings/organizationList/OrganizationList.module.scss';
import OrganizationListItem from 'page/adminSettings/organizationList/OrganizationListItem';
import React from 'react';
import OrganizationRep from 'rep/OrganizationRep';

interface Props {
  organizations: OrganizationRep[];
}

const OrganizationList: React.FC<Props> = ({ organizations }) => {
  return (
    <div className={styles.list}>
      {
        organizations.length > 0
          ? organizations.map((organization) => {
            return <OrganizationListItem key={organization.id} organization={organization} />;
          })
          : <Paragraph>{'No organizations found.'}</Paragraph>
      }
    </div>
  );
};

export default OrganizationList;
