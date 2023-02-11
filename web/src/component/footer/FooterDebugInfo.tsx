import Code from 'component/code/Code';
import Paragraph from 'component/text/Paragraph';
import { useDebugSettings } from 'hook/useDebugSettings';
import React from 'react';
import { useRecoilValueLoadable } from 'recoil';
import organizationGuidState from 'state/core/organizationGuid';

interface Line {
  label: string;
  value: string;
}

const FooterDebugInfo: React.FC = () => {
  const { gitSha } = useDebugSettings();
  const organizationGuid = useRecoilValueLoadable(organizationGuidState).valueMaybe();

  const lines: Line[] = [{ label: 'Git SHA', value: gitSha }];
  if (organizationGuid) lines.push({ label: 'Organization GUID', value: organizationGuid });

  return (
    <Paragraph size="small">
      {
        lines.map((line) => (
          <React.Fragment key={line.label}>{`${line.label}: `}
            <Code selectAll={true}>{line.value}</Code>
            <br />
          </React.Fragment>
        ))
      }
    </Paragraph>
  );
};

export default FooterDebugInfo;
