package bego.market.belbies.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Models.Reports
import bego.market.belbies.R


class ReportAdapter(private val context: Context?, var list:MutableList<Reports>, val deleteOnClickListener: DeleteOnClickListener) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>( ) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_users_report, parent, false)
        return ReportViewHolder(view)
    }


    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = list[position]
         holder.numberUser.text = report.userReport
        holder.textReport.text = report.textReport

        holder.deleteButton.setOnClickListener {
            deleteOnClickListener.clickListener(report)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ReportViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var numberUser: TextView = itemView.findViewById(R.id.number_user)
        var textReport: TextView = itemView.findViewById(R.id.text_report)
        var deleteButton: Button = itemView.findViewById(R.id.report_delete)
    }

    class DeleteOnClickListener(val clickListener: (reports: Reports) -> Unit) {
    }





}
