import Code from 'component/code/Code';
import Container from 'component/container/Container';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React, { ReactNode } from 'react';
import { useRecoilValueLoadable } from 'recoil';
import organizationGuidState from 'state/core/organizationGuid';

interface Line {
  label: string;
  value: ReactNode;
}

/**
 * Uses {@link useRecoilValueLoadable} to ensure it can render even in an erroneous state.
 */
const FooterDebugInfo: React.FC = () => {
  const { gitSha } = useDebugSettings();
  const organizationGuid = useRecoilValueLoadable(organizationGuidState).valueMaybe();

  const lines: Line[] = [{ label: 'Git SHA', value: <Code selectAll={true}>{gitSha}</Code> }];
  if (organizationGuid) {
    lines.push({ label: 'Organization GUID', value: <Code selectAll={true}>{organizationGuid}</Code> });
  }

  return (
    <Container density="very-compact" direction="vertical">
      {
        lines.map((line) => {
          return <Paragraph key={line.label} size="small">{`${line.label}: `}{line.value}</Paragraph>;
        })
      }
    </Container>
  );
};

export default FooterDebugInfo;
