import CodeBlock from 'component/code/CodeBlock';
import HeaderSection from 'component/section/HeaderSection';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React, { ReactNode } from 'react';

interface Props {
  error: Error;
}

/**
 * This error component should be shown when something goes wrong at the page-level.
 */
const ErrorMain: React.FC<Props> = ({ error }) => {
  const { showDebugMessages } = useDebugSettings();

  return (
    <HeaderSection title="Error">
      <Paragraph>{'Something went wrong while loading the page.'}</Paragraph>
      {
        showDebugMessages
          ? <CodeBlock>{error.stack}</CodeBlock>
          : <Paragraph>{'Our team has been notified.'}</Paragraph>
      }
    </HeaderSection>
  );
};

const fallback = (error: Error): ReactNode => <ErrorMain error={error} />;

export default Object.assign(ErrorMain, { fallback });
