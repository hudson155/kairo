import { defaultAuth0OrganizationName } from 'page/adminSettings/editOrganization/authInformation/CreateAuthInformation';

it('defaultAuth0OrganizationName', () => {
  expect(defaultAuth0OrganizationName('   Pierre et Paul $ jouent aux dés'))
    .toBe('pierre-et-paul-dollar-jouent-aux-des');
});
