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
                    "FirstName $i",
                    "LastName $i",
                    debt
                )
            )
        }
        val mListView = findViewById<ListView>(R.id.debtList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, debtors)
        mListView.adapter = adapter
    }

    private fun getListViewList(): MutableList<Debtor> {
        val list = ArrayList<Debtor>()
        val adapter = findViewById<ListView>(R.id.debtList).adapter
        for (i in 0 until adapter.count) {
            list.add((adapter.getItem(i) as Debtor))
        }
        return list
    }

    private fun updateTotalDebt() {
        var newTotalDebt = 0.0
        val debtors = getListViewList()
        for (debtor in debtors) {
            newTotalDebt += debtor.debtAmount
        }
        findViewById<TextView>(R.id.totalDebt).text = newTotalDebt.toString()
    }

    override fun onForgiveDebt(debtor: Debtor) {
        val debtors = getListViewList()
        debtors.remove(debtor)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, debtors)
        findViewById<ListView>(R.id.debtList).adapter = adapter
        updateTotalDebt()
    }

    fun onAddDebtorClick(v: View) {
        val intent = Intent(this, ModifyDebtorActivity::class.java)
        startActivityForResult(intent, 200)
    }
}
