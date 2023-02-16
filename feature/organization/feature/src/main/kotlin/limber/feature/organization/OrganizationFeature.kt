package limber.feature.organization

import com.google.inject.PrivateBinder
import limber.client.feature.FeatureClient
import limber.client.feature.HttpFeatureClient
import limber.client.feature.LocalFeatureClient
import limber.client.organization.HttpOrganizationClient
import limber.client.organization.LocalOrganizationClient
import limber.client.organization.OrganizationClient
import limber.client.organizationAuth.HttpOrganizationAuthClient
import limber.client.organizationAuth.LocalOrganizationAuthClient
import limber.client.organizationAuth.OrganizationAuthClient
import limber.client.organizationHostname.HttpOrganizationHostnameClient
import limber.client.organizationHostname.LocalOrganizationHostnameClient
import limber.client.organizationHostname.OrganizationHostnameClient
import limber.endpoint.feature.CreateFeature
import limber.endpoint.feature.DeleteFeature
import limber.endpoint.feature.GetFeature
import limber.endpoint.feature.ListFeaturesByOrganization
import limber.endpoint.feature.UpdateFeature
import limber.endpoint.organization.CreateOrganization
import limber.endpoint.organization.GetOrganization
import limber.endpoint.organization.ListAllOrganizations
import limber.endpoint.organization.SearchOrganizations
import limber.endpoint.organization.UpdateOrganization
import limber.endpoint.organizationAuth.CreateOrganizationAuth
import limber.endpoint.organizationAuth.DeleteOrganizationAuth
import limber.endpoint.organizationAuth.GetOrganizationAuth
import limber.endpoint.organizationAuth.GetOrganizationAuthByHostname
import limber.endpoint.organizationAuth.GetOrganizationAuthByOrganization
import limber.endpoint.organizationAuth.UpdateOrganizationAuth
import limber.endpoint.organizationHostname.CreateOrganizationHostname
import limber.endpoint.organizationHostname.DeleteOrganizationHostname
import limber.endpoint.organizationHostname.GetOrganizationHostname
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.RestImplementation
import limber.feature.rest.bindClients
import limber.feature.rest.bindHttpClient
import limber.feature.rest.bindRestEndpoint

public class OrganizationFeature(private val rest: RestImplementation) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindHttpClient(rest)
    bindOrganization(binder)
    bindOrganizationAuth(binder)
    bindOrganizationHostname(binder)
    bindFeature(binder)
  }

  private fun bindOrganization(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetOrganization::class)
    binder.bindRestEndpoint(ListAllOrganizations::class)
    binder.bindRestEndpoint(SearchOrganizations::class)
    binder.bindRestEndpoint(CreateOrganization::class)
    binder.bindRestEndpoint(UpdateOrganization::class)

    binder.bindClients {
      bind(OrganizationClient::class) {
        when (rest) {
          is RestImplementation.Local -> LocalOrganizationClient::class
          is RestImplementation.Http -> HttpOrganizationClient::class
        }
      }
    }
  }

  private fun bindOrganizationAuth(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetOrganizationAuth::class)
    binder.bindRestEndpoint(GetOrganizationAuthByOrganization::class)
    binder.bindRestEndpoint(GetOrganizationAuthByHostname::class)
    binder.bindRestEndpoint(CreateOrganizationAuth::class)
    binder.bindRestEndpoint(UpdateOrganizationAuth::class)
    binder.bindRestEndpoint(DeleteOrganizationAuth::class)

    binder.bindClients {
      bind(OrganizationAuthClient::class) {
        when (rest) {
          is RestImplementation.Local -> LocalOrganizationAuthClient::class
          is RestImplementation.Http -> HttpOrganizationAuthClient::class
        }
      }
    }
  }

  private fun bindOrganizationHostname(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetOrganizationHostname::class)
    binder.bindRestEndpoint(CreateOrganizationHostname::class)
    binder.bindRestEndpoint(DeleteOrganizationHostname::class)

    binder.bindClients {
      bind(OrganizationHostnameClient::class) {
        when (rest) {
          is RestImplementation.Local -> LocalOrganizationHostnameClient::class
          is RestImplementation.Http -> HttpOrganizationHostnameClient::class
        }
      }
    }
  }

  private fun bindFeature(binder: PrivateBinder) {
    binder.bindRestEndpoint(GetFeature::class)
    binder.bindRestEndpoint(ListFeaturesByOrganization::class)
    binder.bindRestEndpoint(CreateFeature::class)
    binder.bindRestEndpoint(UpdateFeature::class)
    binder.bindRestEndpoint(DeleteFeature::class)

    binder.bindClients {
      bind(FeatureClient::class) {
        when (rest) {
          is RestImplementation.Local -> LocalFeatureClient::class
          is RestImplementation.Http -> HttpFeatureClient::class
        }
      }
    }
  }
}
