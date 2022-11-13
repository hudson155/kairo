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
 * It uses [useRecoilValueLoadable] to ensure it can render even in an erroneous state.
 */
const Footer: React.FC = () => {
  const { showDebugMessages } = useDebugSettings();

  return (
    <footer className={styles.outer}>
      <div className={styles.inner}>
        <Paragraph size="small">{COPYRIGHT_TEXT}</Paragraph>
        {showDebugMessages ? <DebugInfo /> : null}
      </div>
    </footer>
  );
};

export default Footer;

const DebugInfo: React.FC = () => {
  const { gitSha } = useDebugSettings();
  const organizationGuid = useRecoilValueLoadable(organizationGuidState);

  return (
    <>
      <Paragraph size="small">
        {`Git SHA: `}<Code selectAll={true}>{gitSha}</Code>
      </Paragraph>
      <Paragraph size="small">
        {`Organization GUID: `}<Code selectAll={true}>{organizationGuid.valueMaybe() ?? `?`}</Code>
      </Paragraph>
    </>
  );
};
