package pl.pjatk.prm.dusigrosz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_debtor.*

class ModifyDebtorActivity : AppCompatActivity() {
    private var isUpdate = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_debtor)
        intent?.let {
            if (it.hasExtra("update")) {
                isUpdate = it.getBooleanExtra("isUpdate", false)
            }
        }
    }

    fun onAcceptClick(v: View) {
        val name = debtorNameField.text.toString().trim()
        val debt = debtorDebtField.text.toString().replace("\\s".toRegex(), "").toDoubleOrNull()
        if (isInputValid(name, debt)) {
            println("Input correct")
            println("name: ${name}, debt:${debt}")
        }
        else {
            println("Error")
            println("name: ${name}, debt:${debt}")
        }
    }

    private fun isInputValid(name: String, debt: Double?): Boolean {
        return !name.isBlank() && debt != null && debt >= 0
    }

    fun onRejectClick(v: View) {

    }
}
