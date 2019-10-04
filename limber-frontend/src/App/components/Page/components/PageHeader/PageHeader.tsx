import { ComponentChildren, FunctionalComponent, h } from 'preact';

interface Props {
  children: ComponentChildren
}

const PageHeader: FunctionalComponent<Props> = (props: Props) => {
  return <header>{props.children}</header>;
};

export default PageHeader;
