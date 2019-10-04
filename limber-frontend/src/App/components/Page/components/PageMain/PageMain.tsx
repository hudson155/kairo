import { FunctionalComponent, h, VNode } from 'preact';

interface Props {
  children: VNode<any>
}

const PageMain: FunctionalComponent<Props> = (props: Props) => {
  return <main>{props.children}</main>;
};

export default PageMain;
