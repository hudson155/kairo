import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import Menu from 'component/menu/Menu';
import styles from 'component/menu/Menu.stories.module.scss';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import ProfilePhoto from 'component/profilePhoto/ProfilePhotoDelegate';
import { doNothing } from 'helper/doNothing';
import React, { ComponentProps } from 'react';
import { Link } from 'react-router-dom';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [Decorator.browserRouter()],
} as ComponentMeta<typeof Menu>;

const Template: Story<ComponentProps<typeof Menu>> = () => {
  return (
    <div className={styles.container}>
      <MenuImpl />
      <MenuImpl side="right" />
    </div>
  );
};

export const Default = Template.bind({});

interface Props {
  side?: 'right';
}

const MenuImpl: React.FC<Props> = ({ side = undefined }) => {
  const button = (
    <Button variant="unstyled" onClick={doNothing}>
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
      <Icon className={styles.expandIcon} name="expand_more" />
    </Button>
  );

  return (
    <Menu button={button} side={side}>
      <MenuItems>
        <MenuItem>
          {
            ({ className }) => (
              <Link className={className} to="/first">
                <Icon name="grade" size="small" />
                First
              </Link>
            )
          }
        </MenuItem>
        <MenuItem>
          {
            ({ className }) => (
              <Link className={className} to="/second">
                <Icon name="favorite" size="small" />
                Second
              </Link>
            )
          }
        </MenuItem>
      </MenuItems>
      <MenuItems>
        <MenuItem>
          {
            ({ className }) => (
              <Button className={className} variant="unstyled" onClick={doNothing}>
                <Icon name="close" size="small" />
                Third
              </Button>
            )
          }
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};
