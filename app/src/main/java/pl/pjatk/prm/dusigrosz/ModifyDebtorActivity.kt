package pl.pjatk.prm.dusigrosz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_debtor.*
import pl.pjatk.prm.dusigrosz.models.Debtor

class ModifyDebtorActivity : AppCompatActivity(), RejectDebtorChangesResponse {
    private var debtorID: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_debtor)
        intent?.let {
            if (it.hasExtra("debtorToUpdate")) {
                val debtorToUpdate = it.getSerializableExtra("debtorToUpdate") as Debtor
                debtorID = debtorToUpdate.id
                debtorNameField.text.append(debtorToUpdate.name)
                debtorDebtField.text.append(debtorToUpdate.debtAmount.toString())
            }
        }
    }

    fun onAcceptClick(v: View) {
        val name = debtorNameField.text.toString().trim()
        val debt = debtorDebtField.text.toString().toDoubleOrNull()
        clearErrorMessages()
        val inputError = getInputErrors(name, debt)
        if (inputError == DebtorErrors.NO_ERRORS) {
           setResult(
               0,
               Intent()
                   .apply {
                       putExtra("debtor", debt?.let { Debtor(name, it, debtorID) })
                   }
           )
            finish()
        }
        else {
           displayErrors(inputError)
        }
    }

    private fun getInputErrors(name: String, debt: Double?): DebtorErrors {
        if (name.isBlank() && (debt == null || debt < 0)) {
            return DebtorErrors.ALL_ERROR
        }
        if (name.isBlank()) {
            return DebtorErrors.NAME_ERROR
        }
        if (debt == null || debt < 0) {
            return DebtorErrors.DEBT_ERROR
        }
        return DebtorErrors.NO_ERRORS
    }

    private fun displayErrors(inputError: DebtorErrors) {
        when(inputError) {
            DebtorErrors.NAME_ERROR -> debtorNameError.visibility = View.VISIBLE
            DebtorErrors.DEBT_ERROR -> debtorDebtError.visibility = View.VISIBLE
            else -> {
                debtorNameError.visibility = View.VISIBLE
                debtorDebtError.visibility = View.VISIBLE
            }
        }
    }

    private fun clearErrorMessages() {
        debtorNameError.visibility = View.INVISIBLE
        debtorDebtError.visibility = View.INVISIBLE
    }

    fun onRejectClick(v: View) {
        if (debtorID == null) {
            onRejectChanges()
        }
        else {
            RejectDebtorChangesDialog().show(supportFragmentManager, "rejectDebtorChangesDialog")
        }
    }

    override fun onRejectChanges() {
        setResult(1)
        finish()
    }

    fun onSimulateClick(v: View) {
        val name = debtorNameField.text.toString().trim()
        val debt = debtorDebtField.text.toString().toDoubleOrNull()
        clearErrorMessages()
        val inputError = getInputErrors(name, debt)
        if (inputError == DebtorErrors.NO_ERRORS) {
            val intent = Intent(this, DebtPaymentSimulationActivity::class.java)
            intent.apply {
                putExtra("debtorToSimulate", debt?.let { Debtor(name, it) })
            }
            startActivity(intent)
        }
        else {
            displayErrors(inputError)
        }
    }
}

enum class DebtorErrors {
    NO_ERRORS, NAME_ERROR, DEBT_ERROR, ALL_ERROR
}
