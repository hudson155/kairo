import styles from 'component/footer/Footer.module.scss';
import FooterDebugInfo from 'component/footer/FooterDebugInfo';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React from 'react';

const COPYRIGHT_TEXT = '© Jeff Hudson. All rights reserved.';

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
        {showDebugMessages ? <FooterDebugInfo /> : null}
      </div>
    </footer>
  );
};

export default Footer;
