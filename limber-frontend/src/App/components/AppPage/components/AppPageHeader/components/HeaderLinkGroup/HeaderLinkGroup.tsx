import { ComponentChildren, FunctionalComponent, h } from 'preact';

interface Props {
  children: ComponentChildren
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
