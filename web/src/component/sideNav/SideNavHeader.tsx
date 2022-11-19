import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import React from 'react';
import { useSetRecoilState } from 'recoil';
import sideNavIsOpenState from 'state/nav/sideNavIsOpen';
import styles from './SideNavHeader.module.scss';

const SideNavHeader: React.FC = () => {
  const setSideNavIsOpen = useSetRecoilState(sideNavIsOpenState);

  return (
    <div className={styles.container}>
      <Button variant="unstyled" onClick={() => setSideNavIsOpen(false)}>
        <Icon name="close" />
      </Button>
    </div>
  );
};

export default SideNavHeader;
