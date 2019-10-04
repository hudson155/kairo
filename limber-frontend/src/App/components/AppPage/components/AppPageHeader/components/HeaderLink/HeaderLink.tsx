import { FunctionalComponent, h, VNode } from 'preact';
import { Link } from 'preact-router';

interface Props {
  to: string;
  children: string
}

const HeaderLink: FunctionalComponent<Props> = (props: Props) => {
  const style = {
    display: 'flex',
    alignItems: 'center',
    marginRight: '16px',
    color: 'white',
    fontWeight: 'bold',
    textDecoration: 'none',
  };

  return <Link style={style} href={props.to}>{props.children}</Link>;
};

export default HeaderLink;
