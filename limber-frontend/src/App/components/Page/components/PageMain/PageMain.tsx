import { ComponentChildren, FunctionalComponent, h } from 'preact';

interface Props {
  children: ComponentChildren
}

const PageMain: FunctionalComponent<Props> = (props: Props) => {
  const style = {
    flexGrow: 1,
  };

  return <main style={style}>{props.children}</main>;
};

export default PageMain;
