import { FunctionalComponent, h, VNode } from 'preact';

interface Props {
  children: VNode | VNode[]
}

const HeaderLinkGroup: FunctionalComponent<Props> = (props: Props) => {
  const style = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: '32px',
  };

  return <div style={style}>{props.children}</div>;
};

export default HeaderLinkGroup;
