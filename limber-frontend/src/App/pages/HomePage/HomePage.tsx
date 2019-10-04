import { FunctionalComponent, h } from 'preact';
import Page from '../../components/Page/Page';

const HomePage: FunctionalComponent = () => {
  return <Page header={<p>TODO: Header</p>}
               footer={<p>TODO: Footer</p>}>
    <p>Hello, World!</p>
  </Page>;
};

export default HomePage;
