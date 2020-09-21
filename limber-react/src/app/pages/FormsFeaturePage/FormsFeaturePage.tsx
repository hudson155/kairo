import React, { Suspense } from 'react';
import { useFeature } from '../../../provider/FeatureProvider';
import { graphql } from 'babel-plugin-relay/macro';
import { useFragment, useLazyLoadQuery } from 'react-relay/hooks';
import { FormsFeaturePageQuery } from './__generated__/FormsFeaturePageQuery.graphql';
import { FormsFeaturePageFragment_formInstance$key } from './__generated__/FormsFeaturePageFragment_formInstance.graphql';

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

const FormInstanceExample: React.FC<{
  readonly formInstance: FormsFeaturePageFragment_formInstance$key,
}> = (props) => {
  const formInstance = useFragment(fragment, props.formInstance);

  return (
    <p>{formInstance.id} : {formInstance.createdDate}</p>
  );
};

const FormTemplateExample: React.FC = () => {
  const data = useLazyLoadQuery<FormsFeaturePageQuery>(query, { id: 'b1959d44-b39d-453d-aa27-fb4394c1d550' });

  const formInstances = data.node?.formInstances?.edges?.map(edge => (
    <FormInstanceExample key={edge?.node?.id} formInstance={edge?.node!} />),
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
};

const FormsFeaturePage: React.FC = () => {
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
};

export default FormsFeaturePage;
