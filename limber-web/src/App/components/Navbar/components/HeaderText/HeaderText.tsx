import React, { CSSProperties } from 'react';
import { connect } from 'react-redux';
import { assertLoaded } from '../../../../../redux/util/LoadableState';
import State from '../../../../../state';

interface Props {
  color: string;
  children: string;
}

const HeaderText: React.FC<Props> = (props: Props) => {
  const style: CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    marginRight: '16px',
    color: 'white',
  };

  return <div style={style}>{props.children}</div>;
};

export default connect((state: State) => ({
  color: assertLoaded(state.theme).navBarLinkColor,
}))(HeaderText);
