import { FunctionalComponent, h, VNode } from 'preact';

interface Props {
  children: VNode
}

const PageFooter: FunctionalComponent<Props> = (props: Props) => {
  return <footer>{props.children}</footer>;
};

export default PageFooter;
