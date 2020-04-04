package pl.pjatk.prm.dusigrosz

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.pjatk.prm.dusigrosz.models.Debtor

class ForgiveDebtDialog : DialogFragment() {
    lateinit var debtor: Debtor
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.forgiveDebtDialogTitle))
            .setMessage("Dług ${debtor.name} wynosi ${debtor.debtAmount}")
            .setPositiveButton(getString(R.string.yesMessage)) { _, _ ->
                activity?.let {
                    if (it is ForgiveDebtResponse) {
                        it.onForgiveDebt(debtor)
                    }
                }
            }
            .setNegativeButton(getString(R.string.noMessage)) { _, _ ->}
            .create()
    }
}