import React from 'react';
import { connect } from 'react-redux';
import State from '../../../../../state';
import HeaderLink from '../../../../components/Navbar/components/HeaderLink/HeaderLink';
import HeaderLinkGroup
  from '../../../../components/Navbar/components/HeaderLinkGroup/HeaderLinkGroup';
import { AnyAction } from 'redux';
import HeaderText from '../../../../components/Navbar/components/HeaderText/HeaderText';
import Navbar from '../../../../components/Navbar/Navbar';
import { ThunkDispatch } from 'redux-thunk';

interface Props {
  name?: string;
  dispatch: ThunkDispatch<State, null, AnyAction>;
}

const MainAppNavbar: React.FC<Props> = (props: Props) => {
  return <Navbar
    left={<>
      <HeaderLinkGroup>
        <HeaderLink to="/events">Limber</HeaderLink>
      </HeaderLinkGroup>
      <HeaderLinkGroup>
        <HeaderLink to="/events">Events</HeaderLink>
      </HeaderLinkGroup>
    </>}
    right={
      <>
        <HeaderLinkGroup>
          {props.name && <HeaderText>{props.name}</HeaderText>}
          <HeaderLink to="/signout">Sign Out</HeaderLink>
        </HeaderLinkGroup>
      </>
    } />;
};

export default connect((state: State) => ({
  name: state.user.user?.name,
}))(MainAppNavbar);
