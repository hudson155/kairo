/** @jsxImportSource @emotion/react */

import { CSSObject, Theme } from '@emotion/react';
import React, { useState } from 'react';
import { useUser } from '../../../provider/AuthenticatedStateProvider/UserProvider';
import InlineIcon from '../InlineIcon';
import ProfilePhoto from '../ProfilePhoto';
import SettingsSubnav from './SettingsSubnav';

const styles = {
  buttonContainer: (): CSSObject => ({
    display: 'flex',
    flexDirection: 'row',
  }),
  button: (theme: Theme): CSSObject => ({
    ...theme.util.buttonStyleReset,
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    '> *:first-child': {
      marginRight: theme.size.$4,
    },
    cursor: 'pointer',
  }),
  caret: (theme: Theme): CSSObject => ({
    color: theme.color.app.border.normal,
  }),
};

/**
 * The subnav drops down underneath some component in the nav. This is the only subnav in the app.
 * If other subnavs are required later, this component could be generified.
 */
const SettingsNavDropdown: React.FC = () => {
  const user = useUser();

  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div css={styles.buttonContainer}>
      <button css={styles.button} onClick={() => setIsExpanded(!isExpanded)}>
        <ProfilePhoto placeholder={user.initials} url={user.profilePhotoUrl} />
        <InlineIcon customCss={styles.caret} name="caret-down" />
      </button>
      {isExpanded && <SettingsSubnav onHide={() => setIsExpanded(false)} />}
    </div>
  );
};

export default SettingsNavDropdown;
