import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import React, { MouseEventHandler } from 'react';
import styles from './SideNavHeader.module.scss';

interface Props {
  onClose: MouseEventHandler<HTMLButtonElement>;
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
