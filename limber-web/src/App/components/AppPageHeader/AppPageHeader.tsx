import React from 'react';
import { connect } from 'react-redux';
import State from '../../../state';
import HeaderLink from './components/HeaderLink/HeaderLink';
import HeaderLinkGroup from './components/HeaderLinkGroup/HeaderLinkGroup';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';
import HeaderText from './components/HeaderText/HeaderText';
import Navbar from '../Navbar/Navbar';

interface Props {
  name?: string;
  dispatch: ThunkDispatch<{}, {}, AnyAction>;
}

const AppPageHeader: React.FC<Props> = (props: Props) => {
  return (
    <Navbar>
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
    </Navbar>
  );
};

export default connect((state: State) => ({
  name: state.user.user?.name,
}))(AppPageHeader);
