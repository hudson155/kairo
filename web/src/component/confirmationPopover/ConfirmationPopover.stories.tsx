import { Meta, Story } from '@storybook/react';
import Button from 'component/button/Button';
import ConfirmationPopover from 'component/confirmationPopover/ConfirmationPopover';
import Container from 'component/container/Container';
import Paper from 'component/paper/Paper';
import Paragraph from 'component/text/Paragraph';
import { ComponentProps, useState } from 'react';
import * as Decorator from 'story/Decorator';

type StoryProps = ComponentProps<typeof ConfirmationPopover>;

const story: Meta<StoryProps> = {
  decorators: [Decorator.toastProvider()],
};

export default story;

const Template: Story<StoryProps> = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <Paper>
      <Container direction="vertical">
        <Paragraph>{'Some content within a paper component.'}</Paragraph>
        <Button variant="primary" onClick={() => setIsOpen(true)}>{'Open confirmation popover'}</Button>
      </Container>
      <ConfirmationPopover
        isOpen={isOpen}
        variant="danger"
        onCancel={() => setIsOpen(false)}
        onConfirm={() => setIsOpen(false)}
      >
        <Paragraph>{'Some content within the confirmation popover.'}</Paragraph>
      </ConfirmationPopover>
    </Paper>
  );
};

export const Default = Template.bind({});
