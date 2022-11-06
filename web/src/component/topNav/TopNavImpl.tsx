import { Auth0Client } from '@auth0/auth0-spa-js';
import Button from 'component/button/Button';
import TopNav from 'component/topNav/TopNav';
import React, { ReactNode } from 'react';
import { Link } from 'react-router-dom';
import { useRecoilValueLoadable } from 'recoil';
import FeatureRep from 'rep/FeatureRep';
import OrganizationRep from 'rep/OrganizationRep';
import auth0ClientState from 'state/auth/auth0Client';
import defaultFeatureState from 'state/core/defaultFeature';
import organizationState from 'state/core/organization';

/**
 * This is the functional implementation of the top navigation bar.
 * It uses [useRecoilValueLoadable] to ensure it can render even in an erroneous state.
 */
const TopNavImpl: React.FC = () => {
  const organization = useRecoilValueLoadable(organizationState);
  const defaultFeature = useRecoilValueLoadable(defaultFeatureState);
  const auth0 = useRecoilValueLoadable(auth0ClientState);

  return (
    <TopNav
      left={left(organization.valueMaybe(), defaultFeature.valueMaybe())}
      right={right(auth0.valueMaybe())}
    />
  );
};

export default TopNavImpl;

const left = (organization: OrganizationRep | undefined, defaultFeature: FeatureRep | undefined): ReactNode => {
  if (!organization) return null;
  if (!defaultFeature) return organization.name;
  return <Link to={defaultFeature.rootPath}>{organization.name}</Link>;
};

const right = (auth0: Auth0Client | undefined): ReactNode => {
  if (!auth0) return null;
  return <Button variant="unstyled" onClick={() => void auth0.logout()}>{`Log out`}</Button>;
};
