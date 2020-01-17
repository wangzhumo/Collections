package com.wangzhumo.app.module.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.wangzhumo.app.module.media.model.ActivityItem
import com.wangzhumo.app.widget.rv.OnItemActionListener

/**
 * If you have any questions, you can contact by email {wangzhumoo@gmail.com}
 *
 * @author 王诛魔 2020-01-02  17:20
 *
 * List中Activity的Item
 */
class ActivityListBinder(val listener: OnItemActionListener) : ItemViewBinder<ActivityItem, ActivityListBinder.ActItemViewHolder>() {

    inner class ActItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.tv_title)
        val desc : TextView = itemView.findViewById(R.id.tv_desc)
    }

    override fun onBindViewHolder(holder: ActItemViewHolder, item: ActivityItem) {
        holder.title.text = item.title
        holder.desc.text = item.desc
        holder.itemView.setOnClickListener {
            listener.onAction(getPosition(holder), item)
        }
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ActItemViewHolder {
        return ActItemViewHolder(inflater.inflate(R.layout.item_media_act_item,parent,false))
    }
}