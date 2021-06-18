import { ClassNames, CSSObject, Theme } from '@emotion/react';
import React from 'react';
import { NavLink } from 'react-router-dom';
import FeatureRep from '../../../rep/FeatureRep';
import NavbarItem from './NavbarItem';

const styles = {
  activeLink: (theme: Theme): CSSObject => ({
    borderBottom: `${theme.size.$2} solid ${theme.color.app.navigationHighlight}`,
  }),
};

interface Props {
  readonly feature: FeatureRep.Complete;
}

const FeatureNavLink: React.FC<Props> = ({ feature }) => {
  return (
    <NavbarItem>
      <ClassNames>
        {({ css, theme }) =>
          <NavLink to={feature.path} activeClassName={css(styles.activeLink(theme))}>
            {feature.name}
          </NavLink>
        }
      </ClassNames>
    </NavbarItem>
  );
};

export default FeatureNavLink;