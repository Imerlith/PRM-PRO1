package pl.pjatk.prm.dusigrosz.models

data class Debtor(var firstName: String, var lastName: String, var debtAmount: Double) {
    override fun toString(): String {
        return "$firstName $lastName dług: $debtAmount"
    }
}