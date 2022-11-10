import Button from 'component/button/Button';
import TopNav from 'component/topNav/TopNav';
import React from 'react';
import { Link } from 'react-router-dom';
import { useRecoilValueLoadable } from 'recoil';
import auth0ClientState from 'state/auth/auth0Client';
import defaultFeatureState from 'state/core/defaultFeature';
import organizationState from 'state/core/organization';

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

  if (!organization) return null;
  return (
    <>
      {
        defaultFeature
          ? <Link to={defaultFeature.rootPath}>{organization.name}</Link>
          : organization.name
      }
    </>
  );
};

const Right: React.FC = () => {
  const auth0 = useRecoilValueLoadable(auth0ClientState).valueMaybe();
  if (!auth0) return null;
  return <Button variant="unstyled" onClick={() => void auth0.logout()}>{`Log out`}</Button>;
};
