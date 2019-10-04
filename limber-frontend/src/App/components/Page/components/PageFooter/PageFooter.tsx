import { ComponentChildren, FunctionalComponent, h } from 'preact';

interface Props {
  children: ComponentChildren
}

const PageFooter: FunctionalComponent<Props> = (props: Props) => {
  return <footer>{props.children}</footer>;
};

export default PageFooter;
