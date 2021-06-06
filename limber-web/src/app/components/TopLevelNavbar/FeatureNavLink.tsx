import { ClassNames } from '@emotion/react';
import React from 'react';
import { NavLink } from 'react-router-dom';
import FeatureRep from '../../../rep/FeatureRep';
import styles from './styles';

interface Props {
  readonly feature: FeatureRep.Complete;
}

const FeatureNavLink: React.FC<Props> = ({ feature }) => {
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
