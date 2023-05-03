import { Meta, Story } from '@storybook/react';
import OrganizationList from 'page/adminSettings/organizationList/OrganizationList';
import { ComponentProps } from 'react';
import OrganizationRep from 'rep/OrganizationRep';
import { LOCAL_STATE_CONTEXT_FIXTURE_STATE_ARG_TYPE, LocalStateContextFixtureState, useLocalStateContextFixture } from 'state/local/LocalStateContextFixture';
import { context } from 'state/local/organization/OrganizationProvider';

interface Args {
  state: LocalStateContextFixtureState;
}

type StoryProps = ComponentProps<typeof OrganizationList> & Args;

const story: Meta<StoryProps> = {
  argTypes: {
    state: LOCAL_STATE_CONTEXT_FIXTURE_STATE_ARG_TYPE,
  },
};

export default story;

const Template: Story<StoryProps> = ({ state }) => {
  const value = useLocalStateContextFixture<Map<string, OrganizationRep>>(state, new Map(), new Map([
    { guid: '9d45d538-8131-44cc-aeba-9bef4e045d21', name: 'Acme Co.' },
    { guid: '15cf1689-6322-40a3-92cb-058834d1c8df', name: 'Universal Exports' },
  ].map((organization) => [organization.guid, organization])));

  return (
    <context.Provider value={value}>
      <OrganizationList />
    </context.Provider>
  );
};

export const Default = Template.bind({});
Default.args = {
  state: 'Normal',
};
