import { Fragment, FunctionalComponent, h, VNode } from 'preact';
import PageHeader from './components/PageHeader/PageHeader';
import PageMain from './components/PageMain/PageMain';
import PageFooter from './components/PageFooter/PageFooter';

interface Props {
  header?: VNode
  children: VNode
  footer?: VNode
}

const Page: FunctionalComponent<Props> = (props: Props) => {
  return <Fragment>
    {props.header && <PageHeader>{props.header}</PageHeader>}
    {props.children && <PageMain>{props.children}</PageMain>}
    {props.footer && <PageFooter>{props.footer}</PageFooter>}
  </Fragment>;
};

export default Page;
