import React, { Fragment, ReactNode } from 'react';
import PageFooter from './components/PageFooter/PageFooter';
import PageHeader from './components/PageHeader/PageHeader';
import PageMain from './components/PageMain/PageMain';

interface Props {
  header?: ReactNode;
  children: ReactNode;
  footer?: ReactNode;
}

const Page: React.FC<Props> = (props: Props) => {
  return (
    <Fragment>
      {props.header && <PageHeader>{props.header}</PageHeader>}
      {props.children && <PageMain>{props.children}</PageMain>}
      {props.footer && <PageFooter>{props.footer}</PageFooter>}
    </Fragment>
  );
};

export default Page;
