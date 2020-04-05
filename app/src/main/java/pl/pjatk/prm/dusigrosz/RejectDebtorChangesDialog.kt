package pl.pjatk.prm.dusigrosz

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class RejectDebtorChangesDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rejectDebtorChangesDialogTitle))
            .setMessage(getString(R.string.rejectDebtorChangesDialogMessage))
            .setPositiveButton(getString(R.string.yesMessage)) { _, _ ->
                activity?.let {
                    if (it is RejectDebtorChangesResponse) {
                        it.onRejectChanges()
                    }
                }
            }
            .setNegativeButton(getString(R.string.noMessage)) { _, _ ->}
            .create()
    }
}