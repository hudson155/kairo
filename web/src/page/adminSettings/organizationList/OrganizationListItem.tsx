import Code from 'component/code/Code';
import Text from 'component/text/Text';
import styles from 'page/adminSettings/organizationList/OrganizationListItem.module.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import OrganizationRep from 'rep/OrganizationRep';

interface Props {
  organization: OrganizationRep;
}

const OrganizationListItem: React.FC<Props> = ({ organization }) => {
  return (
    <div className={styles.container}>
      <Link className={styles.name} to={organization.id}>
        <Text size="large">{organization.name}</Text>
      </Link>
      <Text size="small"><Code selectAll={true}>{organization.id}</Code></Text>
    </div>
  );
};

export default OrganizationListItem;
