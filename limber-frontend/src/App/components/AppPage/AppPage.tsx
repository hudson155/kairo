import { FunctionalComponent, h, VNode } from 'preact';
import Page from '../../components/Page/Page';
import AppPageHeader from './components/AppPageHeader/AppPageHeader';

interface Props {
  children: VNode<any>
}

const AppPage: FunctionalComponent<Props> = (props: Props) => {
  return <Page header={<AppPageHeader />}
               footer={<p>TODO: Footer</p>}>
    {props.children}
  </Page>;
};

export default AppPage;
