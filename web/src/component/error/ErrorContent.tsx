import CodeBlock from 'component/code/CodeBlock';
import Container from 'component/container/Container';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React, { ReactNode } from 'react';

interface Props {
  error: unknown;
  message: string;
}

const ErrorContent: React.FC<Props> = ({ error, message }) => {
  const { showDebugMessages } = useDebugSettings();

  return (
    <Container direction="vertical">
      <Paragraph>{message}</Paragraph>
      {debugData(showDebugMessages, error)}
    </Container>
  );
};

export default ErrorContent;

const debugData = (showDebugMessages: boolean, error: unknown): ReactNode => {
  if (!showDebugMessages) {
    // Debug messages won't show to most users.
    return null;
  }

  if (error instanceof Error) {
    // If it's an error, just render it!
    return <CodeBlock>{error.stack}</CodeBlock>;
  }

  // If it's something else, we're out of luck.
  return null;
};
