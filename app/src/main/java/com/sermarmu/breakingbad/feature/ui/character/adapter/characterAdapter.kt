package com.sermarmu.breakingbad.feature.ui.character.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sermarmu.breakingbad.R
import com.sermarmu.breakingbad.extensions.loadImageFromUrl
import com.sermarmu.domain.model.CharacterModel
import kotlinx.android.synthetic.main.character_model.view.*
import java.util.*

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {
    class ViewHolder(
        parent:ViewGroup
    ): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(
                R.layout.character_header_adapter,
                parent,
                false
            )
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = Unit

    override fun getItemCount(): Int = 1
}

class Adapter: ListAdapter<CharacterModel, Adapter.ViewHolder>(
    AdapterDiffCallback
) {
    class ViewHolder(
        v: View
    ): RecyclerView.ViewHolder(v) {
        fun bind(
            model: CharacterModel
        ) = with(itemView) {
            mtv_name_charactermodel.text = model.name
            mtv_status_charactermodel.text = model.status.toString()
            aciv_image_charactermodel.loadImageFromUrl(
                model.img
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(
                R.layout.character_model,
                parent,
                false
            )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        getItem(position)!!.also {
            holder.bind(it)
        }
    }

    private object AdapterDiffCallback : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(
            oldItem: CharacterModel,
            newItem: CharacterModel
        ): Boolean = oldItem.charId == newItem.charId

        override fun areContentsTheSame(
            oldItem: CharacterModel,
            newItem: CharacterModel
        ): Boolean = oldItem.charId == newItem.charId &&
                oldItem.name == newItem.name &&
                oldItem.img == newItem.img &&
                oldItem.status == newItem.status
    }

}