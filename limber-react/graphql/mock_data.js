/* eslint-disable */
const formInstances = [
  {
    __typename: 'FormInstance',
    id: '87a2641f-6dd8-4cb3-a8cb-449a1e95d77d',
    createdDate: '2020-10-01T12:38:09Z',
    number: 1,
    submittedDate: '2020-10-01T12:38:09Z',
    questions: [
      {
        __typename: 'FormInstanceTextQuestion',
        id: 'd6dc3f5c-b0fd-4706-b808-4c22ca6ec3f8',
        createdDate: '2020-10-01T12:38:09Z',
        text: 'great answer',
        questionId: '8771bf5c-bf84-42dc-ba10-4aacf8350f78',
      },
      {
        __typename: 'FormInstanceTextQuestion',
        id: '75fe75b8-0c36-42b9-bea1-fc4c03d3e4e5',
        createdDate: '2020-10-01T12:38:09Z',
        text: 'poop answer',
        questionId: '7f4e8fb8-39f7-4acd-8a57-1770bb6ab325',
      },
      {
        __typename: 'FormInstanceYesNoQuestion',
        id: '76a0ad2c-d55a-4b1d-9825-db3823913722',
        createdDate: '2020-10-01T12:38:09Z',
        questionId: 'e22d9fba-543a-4070-81d6-63481fffe84f',
        yes: true,
      },
      {
        __typename: 'FormInstanceRadioSelectorQuestion',
        id: '62741ce4-b518-4378-bc9d-9578aa7ac983',
        createdDate: '2020-10-01T12:38:09Z',
        questionId: 'e6c60886-18a3-47be-b3fe-13b2dc68907b',
        selection: 'hey',
      },
      {
        __typename: 'FormInstanceDateQuestion',
        id: '3c88fa8a-8fca-457d-aef5-eb0b937da6c4',
        createdDate: '2020-10-01T12:38:09Z',
        questionId: '4d085259-883e-4ea3-8126-812b4db86ad6',
        date: '2020-10-01T00:00:00Z',
      },
    ],
  },
  {
    __typename: 'FormInstance',
    id: '494b53fc-4874-43a6-a502-835c52bd9cfe',
    createdDate: '2020-10-01T12:38:09Z',
    number: 2,
    submittedDate: '2020-10-01T12:38:09Z',
    questions: [],
  },
];

const formTemplates = [
  {
    __typename: 'FormTemplate',
    id: 'b1959d44-b39d-453d-aa27-fb4394c1d550',
    createdDate: '2020-10-01T12:38:09Z',
    title: 'Super serious form',
    description: 'Not one joke is in this form',
    questions: [
      {
        __typename: 'FormTemplateTextQuestion',
        id: '8771bf5c-bf84-42dc-ba10-4aacf8350f78',
        createdDate: '2020-10-01T12:38:09Z',
        label: 'Stellar question',
        required: true,
        multiline: false,
      },
      {
        __typename: 'FormTemplateTextQuestion',
        id: '7f4e8fb8-39f7-4acd-8a57-1770bb6ab325',
        createdDate: '2020-10-01T12:38:09Z',
        label: 'Poop question',
        required: false,
        multiline: false,
      },
      {
        __typename: 'FormTemplateYesNoQuestion',
        id: 'e22d9fba-543a-4070-81d6-63481fffe84f',
        createdDate: '2020-10-01T12:38:09Z',
        label: 'Yes/No question',
        required: false,
      },
      {
        __typename: 'FormTemplateRadioSelectorQuestion',
        id: 'e6c60886-18a3-47be-b3fe-13b2dc68907b',
        createdDate: '2020-10-01T12:38:09Z',
        label: 'Radio question',
        required: false,
        options: ['hey', 'yo', 'guy'],
      },
      {
        __typename: 'FormTemplateDateQuestion',
        id: '4d085259-883e-4ea3-8126-812b4db86ad6',
        createdDate: '2020-10-01T12:38:09Z',
        label: 'Date question',
        required: false,
      },
    ],
  },
];

const user = {
  id: 'b9e46df7-fdb7-4437-87d3-8f48ca47804c',
  firstName: 'Noah',
  lastName: 'Guld',
  fullName: 'Noah Guld',
  emailAddress: 'noah.guld@limberapp.io',
  profilePhotoUrl: 'https://avatars0.githubusercontent.com/u/8917186?s=460&u=364b0d5270cb9657b4222c0816713831805957c9&v=4',
};

module.exports = {
  formInstances,
  formTemplates,
  user,
};
/* eslint-enable */
