import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import { useCollapsibleSideNav } from 'component/sideNav/SideNav';
import TopNav from 'component/topNav/TopNav';
import TopNavMenu from 'component/topNav/TopNavMenu';
import React, { MouseEventHandler } from 'react';
import { Link } from 'react-router-dom';
import { useRecoilState, useRecoilValueLoadable } from 'recoil';
import defaultFeatureState from 'state/core/defaultFeature';
import organizationNameState from 'state/core/organizationName';
import sideNavIsOpenState from 'state/nav/sideNavIsOpen';

/**
 * This is the functional implementation of the top navigation bar.
 * It uses [useRecoilValueLoadable] to ensure it can render even in an erroneous state.
 */
const TopNavImpl: React.FC = () => {
  return <TopNav left={<Left />} right={<TopNavMenu />} />;
};

export default TopNavImpl;

const Left: React.FC = () => {
  const organizationName = useRecoilValueLoadable(organizationNameState).valueMaybe();
  const defaultFeature = useRecoilValueLoadable(defaultFeatureState).valueMaybe();

  const sideNavIsCollapsible = useCollapsibleSideNav();

  if (!organizationName) return null;

  return (
    <>
      {sideNavIsCollapsible ? <SideNavCollapseButton /> : null}
      {
        defaultFeature
          ? <Link to={defaultFeature.rootPath}>{organizationName}</Link>
          : organizationName
      }
    </>
  );
};

const SideNavCollapseButton: React.FC = () => {
  const [sideNavIsOpen, setSideNavIsOpen] = useRecoilState(sideNavIsOpenState);

  const toggleSideNav: MouseEventHandler<HTMLButtonElement> = () => setSideNavIsOpen((currVal) => !currVal);

  return (
    <Button variant="unstyled" onClick={toggleSideNav}>
      <Icon name={sideNavIsOpen ? 'menu_open' : 'menu'} />
    </Button>
  );
};
