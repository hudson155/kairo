import { FunctionalComponent, h, VNode } from 'preact';

interface Props {
  children: VNode
}

const PageHeader: FunctionalComponent<Props> = (props: Props) => {
  return <header>{props.children}</header>;
};

export default PageHeader;
