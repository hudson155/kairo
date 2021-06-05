import { ClassNames } from '@emotion/react';
import React from 'react';
import { NavLink } from 'react-router-dom';
import FeatureRep from '../../../rep/FeatureRep';
import styles from './styles';

interface FeatureNavLinkProps {
  readonly feature: FeatureRep.Complete;
}

const FeatureNavLink: React.FC<FeatureNavLinkProps> = ({ feature }) => {
  return (
    <ClassNames>
      {({ css, theme }) =>
        <NavLink to={feature.path} activeClassName={css(styles.activeLink(theme))}>
          {feature.name}
        </NavLink>
      }
    </ClassNames>
  );
};

export default FeatureNavLink;
