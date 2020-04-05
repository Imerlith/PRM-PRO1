package pl.pjatk.prm.dusigrosz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_debt_payment_simulation.*
import pl.pjatk.prm.dusigrosz.models.Debtor

class DebtPaymentSimulationActivity : AppCompatActivity() {
    lateinit var thread: Thread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_payment_simulation)
        intent?.let {
            if (it.hasExtra("debtorToSimulate")) {
                val debtorToSimulate = it.getSerializableExtra("debtorToSimulate") as Debtor
                populateFields(debtorToSimulate)
            }
        }
    }

    private fun populateFields(debtor: Debtor) {
        debtorName.text = debtor.name
        debtorDebt.text = debtor.debtAmount.toString()
    }

    fun onSimulationButtonClick(v: View) {
        if (simulationControllButton.text.toString() == getString(R.string.startSimulationButtonText)) {
            val speed = debtPaymentSpeed.text.toString().toDoubleOrNull()
            val provision = debtProvision.text.toString().toDoubleOrNull()
            val errors = validateInput(speed as Double, provision as Double)
            simulationControllButton.text = getString(R.string.stopSimulationButtonText)
            simulateDebtPayment(speed, provision)
        }
        else {
            thread.interrupt()
        }
    }

    private fun simulateDebtPayment(speed: Double, provision: Double) {
        var debt = debtorDebt.text.toString().toDouble()
        var numberOfRates = 0
            thread = Thread {
                try {
                    while (debt > 0) {
                        if (debt < speed) {
                            debt = 0.0
                        }
                        else {
                            debt -= speed
                            debt += debt * (provision / 100 )
                        }
                        numberOfRates++
                        this.runOnUiThread {
                            debtorDebt.text = debt.toString()
                            requiredNumberOfRates.text = numberOfRates.toString()
                        }
                        Thread.sleep(1000)
                    }
                }
                catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    this.runOnUiThread {
                        simulationControllButton.text = getString(R.string.startSimulationButtonText)
                    }
                }
                this.runOnUiThread {
                    simulationControllButton.text = getString(R.string.startSimulationButtonText)
                }
            }
            thread.start()
    }

    private fun validateInput(speed: Double?, provision: Double?) {
        if ((speed == null || speed <= 0) && (provision == null || provision <= 0)) {

        }
        if (speed == null || speed <= 0) {

        }
        if (provision == null || provision <= 0){

        }
        //TODO: Returny
    }
}
