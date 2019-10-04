import { FunctionalComponent, h, VNode } from 'preact';
import Page from '../../components/Page/Page';

interface Props {
  children: VNode<any>
}

const AppPage: FunctionalComponent<Props> = (props: Props) => {
  return <Page header={<p>TODO: Header</p>}
               footer={<p>TODO: Footer</p>}>
    {props.children}
  </Page>;
};

export default AppPage;
