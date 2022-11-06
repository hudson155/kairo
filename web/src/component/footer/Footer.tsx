import Code from 'component/code/Code';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import organizationGuidState from 'state/core/organizationGuid';
import styles from './Footer.module.scss';

const COPYRIGHT_TEXT = `© Jeff Hudson. All rights reserved.`;

/**
 * The footer should show on most pages.
 * It contains debug information when SHOW_DEBUG_MESSAGES is set.
 */
const Footer: React.FC = () => {
  const { gitSha, showDebugMessages } = useDebugSettings();
  const organizationGuid = useRecoilValueLoadable(organizationGuidState);

  const debugInfo = (
    <>
      <Paragraph size="small">
        {`Git SHA: `}<Code>{gitSha}</Code>
      </Paragraph>
      <Paragraph size="small">
        {`Organization GUID: `}<Code>{organizationGuid.valueMaybe() ?? `?`}</Code>
      </Paragraph>
    </>
  );

  return (
    <footer className={styles.outer}>
      <div className={styles.inner}>
        <Paragraph size="small">{COPYRIGHT_TEXT}</Paragraph>
        {showDebugMessages ? debugInfo : null}
      </div>
    </footer>
  );
};

export default Footer;
