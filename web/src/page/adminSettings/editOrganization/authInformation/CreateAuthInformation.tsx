import Banner from 'component/banner/Banner';
import Button from 'component/button/Button';
import Paragraph from 'component/text/Paragraph';
import CreateAuthInformationForm from 'page/adminSettings/editOrganization/authInformation/CreateAuthInformationForm';
import React, { useState } from 'react';
import OrganizationRep from 'rep/OrganizationRep';

interface Props {
  organization: OrganizationRep;
}

const CreateAuthInformation: React.FC<Props> = ({ organization }) => {
  const [showForm, setShowForm] = useState(false);

  if (!showForm) {
    return (
      <>
        <Banner variant="warning">
          <Paragraph>{'This organization doesn\'t have auth configured.'}</Paragraph>
        </Banner>
        <Button variant="primary" onClick={() => setShowForm(true)}>{'Configure auth'}</Button>
      </>
    );
  }

  return <CreateAuthInformationForm organization={organization} onCancel={() => setShowForm(false)} />;
};

export default CreateAuthInformation;
