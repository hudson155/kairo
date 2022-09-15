package limber.feature

import com.google.inject.PrivateBinder
import limber.client.HttpOrganizationClient
import limber.client.LocalOrganizationClient
import limber.client.OrganizationClient
import limber.endpoint.CreateOrganization
import limber.endpoint.GetOrganization
import limber.rest.RestImplementation
import limber.rest.bindClients
import limber.rest.bindHttpClient
import limber.rest.bindRestEndpoint

public class OrganizationFeature(private val rest: RestImplementation) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindHttpClient(rest)
    bindOrganization(binder)
  }

  private fun bindOrganization(binder: PrivateBinder) {
    binder.bindRestEndpoint(CreateOrganization::class)
    binder.bindRestEndpoint(GetOrganization::class)

    binder.bindClients {
      bind(OrganizationClient::class) {
        when (rest) {
          is RestImplementation.Local -> LocalOrganizationClient::class
          is RestImplementation.Http -> HttpOrganizationClient::class
        }
      }
    }
  }
}
