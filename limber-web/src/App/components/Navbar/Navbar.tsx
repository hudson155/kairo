import React, { CSSProperties, ReactNode } from 'react';
import { connect } from 'react-redux';
import State from '../../../state';
import { ThunkDispatch } from 'redux-thunk';
import { AnyAction } from 'redux';

interface Props {
  color: string;
  children: ReactNode;
  dispatch: ThunkDispatch<{}, {}, AnyAction>;
}

const AppPageHeader: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    height: '32px',
    backgroundColor: props.color,
    padding: '16px',
  };

  return <div style={style}>{props.children}</div>;
};

export default connect((state: State) => ({
  color: state.theme.theme.navBarColor,
}))(AppPageHeader);
