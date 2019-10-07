package io.limberapp.backend.module.orgs.model.formTemplate

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.framework.model.Model
import io.limberapp.framework.store.MONGO_ID
import io.limberapp.framework.validation.validation.util.shortText
import io.limberapp.framework.validation.validation.validate
import java.time.LocalDateTime
import java.util.UUID

/**
 * An org represents a single client/tenant/organization's basic information/metadata.
 */
data class Org(
    @JsonProperty(MONGO_ID) override val id: UUID?,
    override val created: LocalDateTime?,
    override val version: Int?,
    val name: String
) : Model<Org>() {

    override fun validate() {
        validate(Org::name) { shortText(allowEmpty = false) }
    }

    @JsonIgnore
    override fun setFields(id: UUID?, created: LocalDateTime?, version: Int?): Org {
        return copy(id = id, created = created, version = version)
    }
}
