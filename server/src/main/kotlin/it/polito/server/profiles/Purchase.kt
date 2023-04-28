package it.polito.server.profiles

import it.polito.server.base.EntityBase
import it.polito.server.products.Product
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class Purchase : EntityBase<Long>(){
    var id: Long? = null

    @ManyToOne
    var customer: Profile? = null

    @ManyToOne
    var ean: Product? = null

    // TODO: pensiamoci
    var warranty: Any? = null
    var length: Int? = null
}