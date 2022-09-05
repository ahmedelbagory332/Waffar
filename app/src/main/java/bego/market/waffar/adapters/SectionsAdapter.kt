package bego.market.waffar.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.R
import bego.market.waffar.models.Section
import com.bumptech.glide.Glide
import javax.inject.Inject


class SectionsAdapter @Inject constructor(val context: Application) : RecyclerView.Adapter<SectionsAdapter.ChooseSectionViewHolder>( ) {

    var onSectionClick: ((section: Section) -> Unit)? = null
    private var sectionsList = mutableListOf<Section>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_item_sections, parent, false)
        return ChooseSectionViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: ChooseSectionViewHolder, position: Int) {
        val section = sectionsList[position]
         holder.sectionName.text = section.name
        Glide.with(context).load(section.image).into(holder.sectionImage)

        holder.itemView.setOnClickListener {
            onSectionClick!!.invoke(section)
        }
    }

    override fun getItemCount(): Int {
        return sectionsList.size
    }

    class ChooseSectionViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var sectionImage: ImageView = itemView.findViewById(R.id.section_image)
        var sectionName: TextView = itemView.findViewById(R.id.section_name)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<Section>){
        sectionsList = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(newList: MutableList<Section>) {
        sectionsList = ArrayList()
        sectionsList.addAll(newList)
        notifyDataSetChanged()
    }



}
