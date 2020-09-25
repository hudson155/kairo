import { graphql } from 'babel-plugin-relay/macro';
import React, { ReactElement, Suspense } from 'react';
import { useFragment, useLazyLoadQuery } from 'react-relay/hooks';

import { useFeature } from '../../../provider/FeatureProvider';

import { FormsFeaturePageFragment_formInstance$key } from './__generated__/FormsFeaturePageFragment_formInstance.graphql';
import { FormsFeaturePageQuery } from './__generated__/FormsFeaturePageQuery.graphql';

const query = graphql`
  query FormsFeaturePageQuery($id: ID!) {
    node(id: $id) {
      ... on FormTemplate {
        title
        createdDate
        formInstances {
          edges {
            node {
              id
              ... FormsFeaturePageFragment_formInstance
            }
          }
        }
      }
    }
  }
`;

const fragment = graphql`
  fragment FormsFeaturePageFragment_formInstance on FormInstance {
    createdDate
    id
  }
`;

function FormInstanceExample(props: {
  readonly formInstance: FormsFeaturePageFragment_formInstance$key | null;
}): ReactElement {
  const formInstance = useFragment(fragment, props.formInstance);

  return (
    <p>{formInstance?.id} : {formInstance?.createdDate}</p>
  );
}

function FormTemplateExample(): ReactElement {
  const data = useLazyLoadQuery<FormsFeaturePageQuery>(query, { id: 'b1959d44-b39d-453d-aa27-fb4394c1d550' });

  const formInstances = data.node?.formInstances?.edges?.map(edge =>
    // TODO: ENG-74: Confirm that fallback to null is the correct way for typescript to handle possibly null fragments
    <FormInstanceExample formInstance={edge?.node ?? null} key={edge?.node?.id} />,
  );

  return (
    <>
      <h2>{data.node?.title} created at: {data.node?.createdDate}</h2>
      <div>
        <h3>Instances:</h3>
        {formInstances}
      </div>
    </>
  );
}

function FormsFeaturePage(): ReactElement {
  const feature = useFeature();

  return (
    <>
      <h1>{feature.name}</h1>
      <p>The forms pages have not been written yet.</p>
      <Suspense fallback={<p>Loading forms template</p>}>
        <FormTemplateExample />
      </Suspense>
    </>
  );
}

export default FormsFeaturePage;
