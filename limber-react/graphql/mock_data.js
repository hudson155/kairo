/* eslint-disable */
const formInstances = [
    {
      id: '87a2641f-6dd8-4cb3-a8cb-449a1e95d77d',
      createdDate: '1/2/3456',
      number: 1,
      submittedDate: '1/2/3456',
    },
    {
      id: '494b53fc-4874-43a6-a502-835c52bd9cfe',
      createdDate: '7/8/9012',
      number: 2,
      submittedDate: '7/8/9012',
    },
  ];

const formTemplates = [
  {
    __typename: 'FormTemplate',
    id: 'b1959d44-b39d-453d-aa27-fb4394c1d550',
    createdDate: '6/9/1969',
    title: 'Super serious form',
    description: 'Not one joke is in this form',
  },
];

const user = {
  id: 'b9e46df7-fdb7-4437-87d3-8f48ca47804c',
  firstName: 'Noah',
  lastName: 'Guld',
  fullName: 'Noah Guld',
  emailAddress: 'noah.guld@limberapp.io',
  profilePhotoUrl: 'https://avatars0.githubusercontent.com/u/8917186?s=460&u=364b0d5270cb9657b4222c0816713831805957c9&v=4',
}

module.exports = {
  formInstances,
  formTemplates,
  user
};
/* eslint-enable */
