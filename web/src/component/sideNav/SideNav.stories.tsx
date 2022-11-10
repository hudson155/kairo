import { ComponentMeta, ComponentStory } from '@storybook/react';
import Button from 'component/button/Button';
import Section from 'component/section/Section';
import SideNavEntry from 'component/sideNav/SideNavEntry';
import { useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import SideNav, { useCollapsibleSideNav } from './SideNav';

export default {
  title: `component/SideNav`,
  decorators: [(story) => <BrowserRouter>{story()}</BrowserRouter>],
} as ComponentMeta<typeof SideNav>;

const Template: ComponentStory<typeof Section> = () => {
  const sideNavIsCollapsible = useCollapsibleSideNav();
  const [isOpen, setIsOpen] = useState(false);

  const toggleSideNav = () => setIsOpen((currVal) => !currVal);

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
