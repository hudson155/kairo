import { ComponentMeta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';
import Banner from './Banner';

export default {} as ComponentMeta<typeof Banner>;

const Template: Story<ComponentProps<typeof Banner>> = () => {
  return (
    <Container direction="vertical">
      <Banner variant="success">Success banner.</Banner>
      <Banner variant="warning">Warning banner.</Banner>
      <Banner variant="danger">Danger banner.</Banner>
    </Container>
  );
};

export const Default = Template.bind({});
