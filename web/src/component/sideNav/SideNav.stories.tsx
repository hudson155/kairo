import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import { ComponentProps, MouseEventHandler, useState } from 'react';
import * as Decorator from 'story/Decorator';
import SideNav, { useCollapsibleSideNav } from './SideNav';

export default {
  decorators: [Decorator.browserRouter()],
} as ComponentMeta<typeof SideNav>;

const Template: Story<ComponentProps<typeof SideNav>> = () => {
  const sideNavIsCollapsible = useCollapsibleSideNav();
  const [isOpen, setIsOpen] = useState(false);

  const toggleSideNav: MouseEventHandler<HTMLButtonElement> = () => setIsOpen((currVal) => !currVal);

  return (
    <>
      {/* TODO: Use a styled button, once they exist. */}
      {
        sideNavIsCollapsible
          ? <Button variant="unstyled" onClick={toggleSideNav}>{isOpen ? `Close` : `Open`}</Button>
          : null
      }
      <SideNav isOpen={isOpen} setIsOpen={setIsOpen}>
        <SideNavEntry label="First" to="/first" />
        <SideNavEntry label="Second" to="/second" />
      </SideNav>
    </>
  );
};

export const Default = Template.bind({});
