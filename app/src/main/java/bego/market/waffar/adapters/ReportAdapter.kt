package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.models.Reports
import bego.market.waffar.R
import javax.inject.Inject


class ReportAdapter @Inject constructor(val context: Application) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>( ) {

    var deleteButtonClick: ((reports: Reports) -> Unit)? = null
    private var reportsList = mutableListOf<Reports>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_users_report, parent, false)
        return ReportViewHolder(view)
    }


    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportsList[position]
        holder.numberUser.text = report.userReport
        holder.textReport.text = report.textReport

        holder.deleteButton.setOnClickListener {
            deleteButtonClick!!.invoke(report)
        }
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    class ReportViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var numberUser: TextView = itemView.findViewById(R.id.number_user)
        var textReport: TextView = itemView.findViewById(R.id.text_report)
        var deleteButton: Button = itemView.findViewById(R.id.report_delete)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<Reports>){
        reportsList = list
        notifyDataSetChanged()
    }

}
