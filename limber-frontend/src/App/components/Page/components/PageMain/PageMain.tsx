import { FunctionalComponent, h, VNode } from 'preact';

interface Props {
  children: VNode
}

const PageMain: FunctionalComponent<Props> = (props: Props) => {
  const style = {
    flexGrow: 1,
  };

  return <main style={style}>{props.children}</main>;
};

export default PageMain;
