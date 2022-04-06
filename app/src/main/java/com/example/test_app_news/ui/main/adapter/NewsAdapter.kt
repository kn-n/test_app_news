package com.example.test_app_news.ui.main.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_app_news.R
import com.example.test_app_news.model.Category
import com.example.test_app_news.model.News
import com.example.test_app_news.model.NewsSQLite
import com.example.test_app_news.ui.main.NewsFragment
import com.example.test_app_news.utils.makeOnlyDate
import com.example.test_app_news.utils.makeTime
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val newsList: ArrayList<NewsSQLite>,
                  private val fragment: NewsFragment,
                  private val date: TextView): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = newsList[position]
        if (news.urlToImg.isNotEmpty()) {
            holder.img.visibility = View.VISIBLE
            Picasso.get().load(news.urlToImg).into(holder.img)
        }
        else holder.img.visibility = View.GONE

        holder.source.text = news.source
        holder.title.text = news.title
        holder.description.text = news.description
        holder.time.text = makeTime(news.publishedAt)

        holder.element.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            startActivity(fragment.requireContext(),browserIntent, null)
        }

        date.text = makeOnlyDate(news.publishedAt)
    }

    override fun getItemCount(): Int = newsList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ShapeableImageView = itemView.findViewById(R.id.image)
        val source: TextView = itemView.findViewById(R.id.source)
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val time: TextView = itemView.findViewById(R.id.time)
        val element: CardView = itemView.findViewById(R.id.element)
    }
}