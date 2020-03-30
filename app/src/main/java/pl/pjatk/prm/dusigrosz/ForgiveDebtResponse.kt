package pl.pjatk.prm.dusigrosz

import pl.pjatk.prm.dusigrosz.models.Debtor

interface ForgiveDebtResponse {
    fun onForgiveDebt(debtor: Debtor)
}