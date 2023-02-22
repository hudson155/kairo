import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import styles from 'component/sideNav/SideNavHeader.module.scss';
import React from 'react';

interface Props {
  onClose: () => void;
}

const SideNavHeader: React.FC<Props> = ({ onClose }) => {
  return (
    <div className={styles.container}>
      <Button variant="unstyled" onClick={onClose}>
        <Icon name="close" />
      </Button>
    </div>
  );
};

export default SideNavHeader;
