interface OrganizationAuthRep {
  guid: string;
  organizationGuid: string;
  auth0OrganizationId: string;
  auth0OrganizationName: string;
}

namespace OrganizationAuthRep {
  export interface Creator {
    auth0OrganizationName: string;
  }

  export interface Updater {
    auth0OrganizationName?: string;
  }
}

export default OrganizationAuthRep;
