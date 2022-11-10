import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import { useCollapsibleSideNav } from 'component/sideNav/SideNav';
import TopNav from 'component/topNav/TopNav';
import React from 'react';
import { Link } from 'react-router-dom';
import { useRecoilState, useRecoilValueLoadable } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';
import defaultFeatureState from 'state/core/defaultFeature';
import organizationState from 'state/core/organization';
import sideNavIsOpenState from 'state/nav/sideNavIsOpen';

/**
 * This is the functional implementation of the top navigation bar.
 * It uses [useRecoilValueLoadable] to ensure it can render even in an erroneous state.
 */
const TopNavImpl: React.FC = () => {
  return (
    <TopNav
      left={<Left />}
      right={<Right />}
    />
  );
};

export default TopNavImpl;

const Left: React.FC = () => {
  const organization = useRecoilValueLoadable(organizationState).valueMaybe();
  const defaultFeature = useRecoilValueLoadable(defaultFeatureState).valueMaybe();

  const sideNavIsCollapsible = useCollapsibleSideNav();

  if (!organization) return null;

  return (
    <>
      {sideNavIsCollapsible ? <SideNavCollapseButton /> : null}
      {
        defaultFeature
          ? <Link to={defaultFeature.rootPath}>{organization.name}</Link>
          : organization.name
      }
    </>
  );
};

const SideNavCollapseButton: React.FC = () => {
  const [sideNavIsOpen, setSideNavIsOpen] = useRecoilState(sideNavIsOpenState);

  const toggleSideNav = () => setSideNavIsOpen((currVal) => !currVal);

  return (
    <Button variant="unstyled" onClick={toggleSideNav}>
      <Icon name={sideNavIsOpen ? `menu_open` : `menu`} />
    </Button>
  );
};

const Right: React.FC = () => {
  const auth0 = useRecoilValueLoadable(auth0ClientState).valueMaybe();
  if (!auth0) return null;
  return <Button variant="unstyled" onClick={() => void auth0.logout()}>{`Log out`}</Button>;
};
