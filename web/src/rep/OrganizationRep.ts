interface OrganizationRep {
  guid: string;
  name: string;
}

namespace OrganizationRep {
  export interface Updater {
    name?: string | null;
  }
}

export default OrganizationRep;
