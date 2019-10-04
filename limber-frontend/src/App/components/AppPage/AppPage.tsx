import { ComponentChildren, FunctionalComponent, h } from 'preact';
import Page from '../../components/Page/Page';
import AppPageHeader from './components/AppPageHeader/AppPageHeader';
import AppPageFooter from './components/AppPageFooter/AppPageFooter';

interface Props {
  children: ComponentChildren
}

const AppPage: FunctionalComponent<Props> = (props: Props) => {
  return <Page header={<AppPageHeader />} footer={<AppPageFooter />}>{props.children}</Page>;
};

export default AppPage;
