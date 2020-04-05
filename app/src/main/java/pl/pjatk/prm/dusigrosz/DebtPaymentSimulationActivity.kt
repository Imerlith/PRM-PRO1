package pl.pjatk.prm.dusigrosz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_debt_payment_simulation.*
import pl.pjatk.prm.dusigrosz.models.Debtor
import kotlin.properties.Delegates

class DebtPaymentSimulationActivity : AppCompatActivity() {
    lateinit var thread: Thread
    private var originalDebt by Delegates.notNull<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_payment_simulation)
        intent?.let {
            if (it.hasExtra("debtorToSimulate")) {
                val debtorToSimulate = it.getSerializableExtra("debtorToSimulate") as Debtor
                populateFields(debtorToSimulate)
                originalDebt = debtorToSimulate.debtAmount
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
            clearFormErrors()
            val errors = validateInput(speed, provision)
            if (errors == SimulationFormError.NO_ERROR) {
                simulationControllButton.text = getString(R.string.stopSimulationButtonText)
                simulateDebtPayment(speed as Double, provision as Double)
            }
            else {
                displayFormErrors(errors)
            }
        }
        else {
            thread.interrupt()
        }
    }

    private fun clearFormErrors() {
        provisionErrorLabel.visibility = View.INVISIBLE
        speedErrorLabel.visibility = View.INVISIBLE
    }

    private fun displayFormErrors(errors: SimulationFormError) {
        when(errors) {
            SimulationFormError.SPEED_ERROR -> speedErrorLabel.visibility = View.VISIBLE
            SimulationFormError.PROVISION_ERROR -> provisionErrorLabel.visibility = View.VISIBLE
            else -> {
                speedErrorLabel.visibility = View.VISIBLE
                provisionErrorLabel.visibility = View.VISIBLE
            }
        }
    }

    private fun simulateDebtPayment(speed: Double, provision: Double) {
        var debt = debtorDebt.text.toString().toDouble()
        requiredNumberOfRatesLabel.visibility = View.VISIBLE
        requiredNumberOfRates.visibility = View.VISIBLE
        var numberOfRates = 0
            thread = Thread {
                try {
                    while (debt > 0) {
                        debt = getRecalculateDebt(debt, speed, provision)
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
                }
                finally {
                    this.runOnUiThread {
                        simulationControllButton.text = getString(R.string.startSimulationButtonText)
                        debtorDebt.text = originalDebt.toString()
                    }
                }
            }
            thread.start()
    }

    private fun getRecalculateDebt(currentDebt: Double, speed: Double, provision: Double): Double {
        var updatedDebt = currentDebt
        if (updatedDebt < speed) {
            updatedDebt = 0.0
        }
        else {
            updatedDebt -= speed
            updatedDebt += updatedDebt * (provision / 100 )
        }
        return updatedDebt
    }

    private fun validateInput(speed: Double?, provision: Double?): SimulationFormError {
        if ((speed == null || speed <= 0) && (provision == null || provision <= 0)) {
            return SimulationFormError.ALL_ERROR
        }
        if (speed == null || speed <= 0) {
            return SimulationFormError.SPEED_ERROR
        }
        if (provision == null || provision <= 0){
            return SimulationFormError.PROVISION_ERROR
        }
        return SimulationFormError.NO_ERROR
    }
}

enum class SimulationFormError {
    NO_ERROR, SPEED_ERROR, PROVISION_ERROR, ALL_ERROR
}
