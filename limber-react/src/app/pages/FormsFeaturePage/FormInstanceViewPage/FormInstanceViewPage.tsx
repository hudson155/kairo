import { CSSObject } from '@emotion/core';
import { graphql } from 'babel-plugin-relay/macro';
import React, { ReactElement } from 'react';
import { useLazyLoadQuery } from 'react-relay/hooks';
import { useRouteMatch } from 'react-router-dom';

import FormRenderer from '../../../components/FormRenderer';
import { EmotionTheme } from '../../../EmotionTheme';

import { FormInstanceViewPageQuery } from './__generated__/FormInstanceViewPageQuery.graphql';

const styles = {
  footer: (theme: EmotionTheme): CSSObject => ({
    display: 'flex',
    flexDirection: 'row-reverse',
    paddingTop: theme.size.$12,
  }),
};

const query = graphql`
  query FormInstanceViewPageQuery($id: ID!) {
    node(id: $id) {
      ... on FormInstance {
        submittedDate
        formTemplate {
          title
        }
        ... FormRendererFragment_formInstance
      }
    }
  }
`;

interface MatchParams {
  readonly id: string;
}

function formInstanceViewPageSubpath(formInstanceID?: string): string {
  return `instances/${formInstanceID ?? ':id'}/view`;
}

function FormInstanceViewPage(): ReactElement {
  const match = useRouteMatch<MatchParams>();
  const data = useLazyLoadQuery<FormInstanceViewPageQuery>(query, match.params);
  const formInstance = data.node;

  if (formInstance == null) {
    return <p>Invalid form instance id.</p>;
  }

  return (
    <>
      <h1>{formInstance.formTemplate?.title}</h1>
      <FormRenderer formInstance={formInstance} />
      <div css={styles.footer}>
        {formInstance.submittedDate && (<div>Submitted At: {new Date(formInstance.submittedDate).toDateString()}</div>)}
      </div>
    </>
  );
}

export { formInstanceViewPageSubpath };
export default FormInstanceViewPage;
