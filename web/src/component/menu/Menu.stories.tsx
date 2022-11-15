import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'component/button/Button';
import Icon from 'component/icon/Icon';
import MenuItem from 'component/menu/MenuItem';
import MenuItems from 'component/menu/MenuItems';
import { Delegate as ProfilePhoto } from 'component/profilePhoto/ProfilePhoto';
import { Link } from 'react-router-dom';
import * as Decorator from 'story/Decorator';
import Menu from './Menu';
import styles from './Menu.stories.module.scss';

export default {
  title: `component/Menu`,
  decorators: [Decorator.browserRouter()],
} as ComponentMeta<typeof Menu>;

const Template: ComponentStory<typeof Menu> = () => {
  const button = (
    <Button variant="unstyled">
      <ProfilePhoto url="https://avatars.githubusercontent.com/u/1360420" />
      <Icon className={styles.expandIcon} name="expand_more" />
    </Button>
  );

  return (
    <Menu button={button}>
      <MenuItems>
        <MenuItem>
          {
            ({ className }) => (
              <Link className={className} to="/first">
                <Icon name="grade" size="small" space="after" />
                First
              </Link>
            )
          }
        </MenuItem>
        <MenuItem>
          {
            ({ className }) => (
              <Link className={className} to="/second">
                <Icon name="favorite" size="small" space="after" />
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
              <Button className={className} variant="unstyled">
                <Icon name="close" size="small" space="after" />
                Third
              </Button>
            )
          }
        </MenuItem>
      </MenuItems>
    </Menu>
  );
};

export const Default = Template.bind({});
