interface OrganizationRep {
  id: string;
  name: string;
}

namespace OrganizationRep {
  export interface Updater {
    name?: string;
  }
}

export default OrganizationRep;
