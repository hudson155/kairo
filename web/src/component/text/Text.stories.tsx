import { ComponentMeta, ComponentStory } from '@storybook/react';
import Heading1 from './Heading1';
import Heading2 from './Heading2';
import Heading3 from './Heading3';
import Heading4 from './Heading4';
import Paragraph from './Paragraph';
import Text from './Text';

export default {
  title: `component/Text`,
} as ComponentMeta<typeof Text>;

// eslint-disable-next-line max-lines-per-function
const Template: ComponentStory<typeof Text> = () => {
  return (
    <>
      <Heading1>Text demonstration</Heading1>
      <Paragraph>
        This story is intended to demonstrate how various types of text look,
        including headings, paragraphs, and other styling.
      </Paragraph>
      <Heading2>Heading 2</Heading2>
      <Paragraph>
        <Text weight="bold">Lorem ipsum</Text> dolor sit amet, consectetur adipiscing elit.
        Suspendisse quis erat erat.
        Donec iaculis feugiat sapien et commodo.
        Mauris ultrices odio quis odio elementum ultrices.
        Cras in ipsum quis arcu facilisis accumsan ut pretium neque.
        Ut fringilla gravida auctor.
        Aliquam rhoncus, quam nec dignissim congue, est nunc gravida nulla, ut tempus augue dui dictum nulla.
      </Paragraph>
      <Paragraph>
        Donec volutpat augue mauris, ut luctus arcu dapibus sed.
        Integer blandit fringilla dolor, in varius dolor.
        Donec hendrerit libero eget turpis iaculis, in iaculis dolor posuere.
        Morbi dapibus nunc eget quam elementum, scelerisque ornare sem lacinia.
        Integer congue tortor libero, nec auctor purus eleifend eget.
        Praesent nec felis sit amet sem bibendum consectetur.
        Aliquam sed venenatis felis.
        Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.
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
    </>
  );
};

export const Default = Template.bind({});
