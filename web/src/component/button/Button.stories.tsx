import { ComponentMeta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import Container from 'component/container/Container';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Button>;

const Template: Story<ComponentProps<typeof Button>> = () => {
  return (
    <Container direction="vertical">
      <Button variant="unstyled">Button text</Button>
      <Button variant="primary">Button text</Button>
      <Button variant="secondary">Button text</Button>
    </Container>
  );
};

export const Default = Template.bind({});
