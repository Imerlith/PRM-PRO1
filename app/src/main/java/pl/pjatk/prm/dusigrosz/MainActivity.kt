package pl.pjatk.prm.dusigrosz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import pl.pjatk.prm.dusigrosz.models.Debtor

class MainActivity : AppCompatActivity(), ForgiveDebtResponse {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generateInitialDebtList()
        updateTotalDebt()
    }

    private fun generateInitialDebtList() {
        val debtors = ArrayList<Debtor>()
        for (i in 1 until 30) {
            val debt = (i+200).toDouble()
            debtors.add(
                Debtor(
                    "FirstName LastName $i",
                    debt
                )
            )
        }
        setAdapterToDebtorList(debtors)
    }

    private fun updateTotalDebt() {
        var newTotalDebt = 0.0
        val debtors = getListViewList()
        for (debtor in debtors) {
            newTotalDebt += debtor.debtAmount
        }
        findViewById<TextView>(R.id.totalDebt).text = newTotalDebt.toString()
    }

    private fun setAdapterToDebtorList(debtors: MutableList<Debtor>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, debtors)
        findViewById<ListView>(R.id.debtList).adapter = adapter
    }

    private fun getListViewList(): MutableList<Debtor> {
        val list = ArrayList<Debtor>()
        val adapter = findViewById<ListView>(R.id.debtList).adapter
        for (i in 0 until adapter.count) {
            list.add((adapter.getItem(i) as Debtor))
        }
        return list
    }

    override fun onForgiveDebt(debtor: Debtor) {
        val debtors = getListViewList()
        debtors.remove(debtor)
        setAdapterToDebtorList(debtors)
        updateTotalDebt()
    }

    fun onAddDebtorClick(v: View) {
        val intent = Intent(this, ModifyDebtorActivity::class.java)
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 0) {
            val debtor = data?.getSerializableExtra("debtor") as Debtor
            if (requestCode == 200 || requestCode == 201) {
                addOrUpdateDebtor(debtor)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun addOrUpdateDebtor(debtor: Debtor) {
        val debtors = getListViewList()
        if (debtors.contains(debtor)) {
            debtors[debtors.indexOf(debtor)] = debtor
        }
        else {
            debtors.add(debtor)
        }
        setAdapterToDebtorList(debtors)
        updateTotalDebt()
    }
}
