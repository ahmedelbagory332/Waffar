package bego.market.belbies.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Models.AllProduct
import bego.market.belbies.Models.Sections
import bego.market.belbies.R
import com.bumptech.glide.Glide


class ChooseSectionsAdapter(private val context: Context?, var list:MutableList<Sections>, val onClickListener: OnClickListener) : RecyclerView.Adapter<ChooseSectionsAdapter.ChooseSectionViewHolder>( ) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_sections, parent, false)
        return ChooseSectionViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val section = list[position]
         holder.sectionName.text = section.name
        Glide.with(context!!).load(section.image).into(holder.sectionImage)

        holder.itemView.setOnClickListener {
            onClickListener.clickListener(section.name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var sectionImage: ImageView = itemView.findViewById(R.id.section_image)
        var sectionName: TextView = itemView.findViewById(R.id.section_name)

    }

    class OnClickListener(val clickListener: (name: String) -> Unit) {
    }


    fun search(newlist: MutableList<Sections>) {
        list = ArrayList()
        list.addAll(newlist)
        notifyDataSetChanged()
    }



}
