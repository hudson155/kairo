import { FunctionalComponent, h } from 'preact';
import Page from '../../components/Page/Page';

const NotFoundPage: FunctionalComponent = () => {
  return <Page header={<p>TODO: Header</p>}
               footer={<p>TODO: Footer</p>}>
    <p>404 Not Found</p>
  </Page>;
};

export default NotFoundPage;
