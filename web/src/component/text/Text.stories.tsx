import { ComponentMeta, Story } from '@storybook/react';
import Container from 'component/container/Container';
import Heading1 from 'component/text/Heading1';
import Heading2 from 'component/text/Heading2';
import Heading3 from 'component/text/Heading3';
import Heading4 from 'component/text/Heading4';
import Paragraph from 'component/text/Paragraph';
import Text from 'component/text/Text';
import { ComponentProps } from 'react';

export default {} as ComponentMeta<typeof Text>;

// eslint-disable-next-line max-lines-per-function
const Template: Story<ComponentProps<typeof Text>> = () => {
  return (
    <Container direction="vertical">
      <Heading1>Text demonstration</Heading1>
      <Paragraph>
        This story is intended to demonstrate how various types of text look,
        including headings, paragraphs, and other styling.
      </Paragraph>
      <Paragraph size="large">
        Sometimes a paragraph is large.&nbsp;
        <Text weight="light">It can have light text</Text>&nbsp;
        <Text weight="bold">and bold text</Text>.
      </Paragraph>
      <Paragraph>
        Sometimes a paragraph is normal.&nbsp;
        <Text weight="light">It can have light text</Text>&nbsp;
        <Text weight="bold">and bold text</Text>.
      </Paragraph>
      <Paragraph size="small">
        Sometimes a paragraph is small.&nbsp;
        <Text weight="light">It can have light text</Text>&nbsp;
        <Text weight="bold">and bold text</Text>.
      </Paragraph>
      <Heading2>Heading 2</Heading2>
      <Paragraph>
        <Text size="large">Other times,</Text>
        &nbsp;a paragraph has multiple sizes&nbsp;
        <Text size="small">all in one!</Text>
      </Paragraph>
      <Heading3>Heading 3</Heading3>
      <Paragraph>
        Donec porta nec tortor non euismod.
        Fusce eget turpis ornare, semper sapien quis, pellentesque ligula.
        Nam vitae elementum purus, viverra ultrices dolor.
        Mauris interdum sit amet nibh nec molestie.
        Vivamus orci eros, pharetra ut sagittis consectetur, ultrices sed magna.
        Donec non feugiat felis.
        Integer sollicitudin sem nunc, eu hendrerit ipsum ultrices accumsan.
      </Paragraph>
      <Heading4>Heading 4</Heading4>
      <Paragraph>
        Mauris sodales sodales leo id varius.
        Aliquam eu sapien ac velit pellentesque pulvinar.
        Integer id tempus mauris.
        Integer vel bibendum risus.
        Praesent erat neque, vehicula a imperdiet vitae, feugiat et lorem.
        Sed enim leo, lobortis id condimentum id, malesuada sit amet mauris.
        In hac habitasse platea dictumst.
      </Paragraph>
    </Container>
  );
};

export const Default = Template.bind({});
