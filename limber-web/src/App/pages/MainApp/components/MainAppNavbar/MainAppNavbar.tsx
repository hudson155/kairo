import React from 'react';
import { connect } from 'react-redux';
import FeatureModel from '../../../../../models/org/FeatureModel';
import State from '../../../../../state';
import HeaderLink from '../../../../components/Navbar/components/HeaderLink/HeaderLink';
import HeaderLinkGroup from '../../../../components/Navbar/components/HeaderLinkGroup/HeaderLinkGroup';
import HeaderText from '../../../../components/Navbar/components/HeaderText/HeaderText';
import Navbar from '../../../../components/Navbar/Navbar';

interface Props {
  features?: FeatureModel[];
  name?: string;
}

const MainAppNavbar: React.FC<Props> = (props: Props) => {

  let defaultFeatureNavLink = <HeaderText>Limber</HeaderText>;
  let featureNavLinks = null;
  if (props.features) {
    featureNavLinks = props.features.map(feature => {
      if (feature.isDefaultFeature) {
        defaultFeatureNavLink = <HeaderLink to={feature.path}>Limber</HeaderLink>;
      }
      return <HeaderLink key={feature.path} to={feature.path}>{feature.name}</HeaderLink>;
    });
  }

  return <Navbar
    left={<>
      <HeaderLinkGroup>{defaultFeatureNavLink}</HeaderLinkGroup>
      <HeaderLinkGroup>{featureNavLinks}</HeaderLinkGroup>
    </>}
    right={
      <>
        <HeaderLinkGroup>
          {props.name && <HeaderText>{props.name}</HeaderText>}
          <HeaderLink to="/signout">Sign Out</HeaderLink>
        </HeaderLinkGroup>
      </>
    } />;
};

export default connect((state: State) => ({
  features: state.org.model?.features,
  name: [state.auth.model?.user.firstName, state.auth.model?.user.lastName].filter(x => Boolean(x)).join(' '),
}))(MainAppNavbar);
