package pl.pjatk.prm.dusigrosz.models

import java.io.Serializable

data class Debtor(var name: String, var debtAmount: Double) : Serializable {
    constructor(name: String, debtAmount: Double, debtorID: Int?) : this(name, debtAmount) {
        id = debtorID?: indexGenerator++
    }
    companion object {
        var indexGenerator = 1;
    }
    var id: Int = indexGenerator++
    override fun toString(): String {
        return "$name dług: $debtAmount"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Debtor

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}