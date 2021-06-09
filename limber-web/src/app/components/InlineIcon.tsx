/** @jsxImportSource @emotion/react */

import { Theme } from '@emotion/react';
import { Interpolation } from '@emotion/serialize';
import React from 'react';

interface Props {
  name: string;
  customCss?: Interpolation<Theme>;
}

/**
 * This currently delegates directly to Font Awesome. As a facade, it aims to enable easy transition
 * to another icon source if/when necessary.
 */
const InlineIcon: React.FC<Props> = ({ name, customCss }) => {
  return <i css={customCss} className={`fa fa-${name}`} />;
};

export default InlineIcon;
