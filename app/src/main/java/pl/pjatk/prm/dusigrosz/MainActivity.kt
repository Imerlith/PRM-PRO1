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
        setListeners()
    }

    private fun generateInitialDebtList() {
        val debtors = mutableListOf<Debtor>(
            Debtor("Jakub Dzeciątko", 1200.0),
            Debtor("Paweł Kalbarczyk", 300.0),
            Debtor("Wojciech Szadurski", 3332.20),
            Debtor("Lena Ambicka", 414.123)
        )
        setAdapterToDebtorList(debtors)
    }

    private fun updateTotalDebt() {
        var newTotalDebt = 0.0
        val debtors = getListViewList()
        for (debtor in debtors) {
            newTotalDebt += debtor.debtAmount
        }
        findViewById<TextView>(R.id.totalDebt).text = "Suma: $newTotalDebt"
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

    private fun setListeners() {
        val listView = findViewById<ListView>(R.id.debtList)
        listView.setOnItemClickListener {
                parent, _, position, _ ->
                val selectedDebtor = parent.getItemAtPosition(position) as Debtor
                val intent = Intent(this, ModifyDebtorActivity::class.java)
                intent.putExtra("debtorToUpdate", selectedDebtor)
                startActivityForResult(intent, 201)
        }
        listView.setOnItemLongClickListener {
                parent, _, position, _ ->
                val selectedDebtor = parent.getItemAtPosition(position) as Debtor
                    ForgiveDebtDialog().apply {
                        debtor = selectedDebtor
                    }.show(supportFragmentManager, "forgiveDebtorDialog")
                true
        }
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
