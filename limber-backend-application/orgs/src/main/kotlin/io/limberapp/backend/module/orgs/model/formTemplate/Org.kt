package io.limberapp.backend.module.orgs.model.formTemplate

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.framework.model.Model
import io.limberapp.framework.store.MONGO_ID
import io.limberapp.framework.validation.validation.util.shortText
import io.limberapp.framework.validation.validation.validate
import java.util.UUID

/**
 * An org represents a single client/tenant/organization's basic information/metadata.
 */
data class Org(
    @JsonProperty(MONGO_ID) override val id: UUID?,
    val name: String
) : Model<Org>() {

    override fun validate() {
        validate(Org::name) { shortText(allowEmpty = false) }
    }

    @JsonIgnore
    override fun setId(id: UUID?) = copy(id = id)
}
