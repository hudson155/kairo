import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import SideNav, { useCollapsibleSideNav } from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import { ComponentProps, useState } from 'react';
import * as Decorator from 'story/Decorator';

export default {
  decorators: [Decorator.browserRouter()],
} as ComponentMeta<typeof SideNav>;

const Template: Story<ComponentProps<typeof SideNav>> = () => {
  const sideNavIsCollapsible = useCollapsibleSideNav();
  const [isOpen, setIsOpen] = useState(false);

  const toggleSideNav = () => setIsOpen((currVal) => !currVal);

  return (
    <>
      {
        sideNavIsCollapsible
          ? <Button variant="primary" onClick={toggleSideNav}>{isOpen ? 'Close' : 'Open'}</Button>
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
