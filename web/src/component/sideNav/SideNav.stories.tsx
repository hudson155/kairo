import { Meta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import SideNav, { useCollapsibleSideNav } from 'component/sideNav/SideNav';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import { ComponentProps, useState } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof SideNav>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.browserRouter()],
};

export default story;

const Template: Story<StoryProps> = () => {
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
