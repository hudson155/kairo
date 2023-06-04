interface OrganizationRep {
  id: string;
  name: string;
}

namespace OrganizationRep {
  export interface Creator {
    name: string;
  }

  export interface Updater {
    name?: string;
  }
}

export default OrganizationRep;
