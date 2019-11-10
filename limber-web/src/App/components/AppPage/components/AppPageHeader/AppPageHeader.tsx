import React, { CSSProperties } from 'react';
import { connect } from 'react-redux';
import State from '../../../../../state';
import HeaderLink from './components/HeaderLink/HeaderLink';
import HeaderLinkGroup from './components/HeaderLinkGroup/HeaderLinkGroup';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import HeaderText from './components/HeaderText/HeaderText';

interface Props {
  color: string;
  name?: string;
  dispatch: ThunkDispatch<{}, {}, AnyAction>;
}

const AppPageHeader: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    height: '32px',
    backgroundColor: props.color,
    padding: '16px',
  };

  return (
    <div style={style}>
      <HeaderLinkGroup>
        <HeaderLink to="/events">Limber</HeaderLink>
      </HeaderLinkGroup>
      <HeaderLinkGroup>
        <HeaderLink to="/events">Events</HeaderLink>
      </HeaderLinkGroup>
      <HeaderLinkGroup>
        {props.name && <HeaderText>{props.name}</HeaderText>}
        <HeaderLink to="/signout">Sign Out</HeaderLink>
      </HeaderLinkGroup>
    </div>
  );
};

export default connect((state: State) => ({
  color: state.theme.theme.navBarColor,
  name: state.user.user?.name,
}))(AppPageHeader);
