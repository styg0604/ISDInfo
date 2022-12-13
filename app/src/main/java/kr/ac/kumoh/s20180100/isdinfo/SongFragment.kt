package kr.ac.kumoh.s20180100.isdinfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView

class SongFragment : Fragment() {
    private lateinit var songModel: SongViewModel

    private var linearLayoutManager: RecyclerView.LayoutManager? = null
    private var songAdapter: RecyclerView.Adapter<SongAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songModel = ViewModelProvider(this)[SongViewModel::class.java]

        songModel.list.observe(this) {
            songAdapter?.notifyItemRangeInserted(0,
                songModel.list.value?.size ?: 0)
        }

        songModel.requestSong()

    }

    inner class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>(){
        //inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        inner class ViewHolder(itemView: View)
            : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            val txTitle: TextView = itemView.findViewById(R.id.text1)
            val txType: TextView = itemView.findViewById(R.id.text2)

            val niImage: NetworkImageView = itemView.findViewById(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                val intent = Intent(context, SongActivity::class.java)
                intent.putExtra(SongActivity.KEY_TITLE,
                    songModel.list.value?.get(adapterPosition)?.title)
                intent.putExtra(SongActivity.KEY_TYPE,
                    songModel.list.value?.get(adapterPosition)?.type)
                intent.putExtra(SongActivity.KEY_IMAGE,
                    songModel.getImageUrl(adapterPosition))
                intent.putExtra(SongActivity.KEY_UPLOAD,
                    songModel.list.value?.get(adapterPosition)?.upload)
                intent.putExtra(SongActivity.KEY_DESCRIPTION,
                    songModel.list.value?.get(adapterPosition)?.description)
                intent.putExtra(SongActivity.KEY_LINK,
                    songModel.list.value?.get(adapterPosition)?.link)
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_song,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txTitle.text = songModel.list.value?.get(position)?.title
            holder.txType.text = songModel.list.value?.get(position)?.type
            holder.niImage.setImageUrl(songModel.getImageUrl(position), songModel.imageLoader)
        }

        override fun getItemCount() = songModel.list.value?.size ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_song, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.song_list)

        songAdapter = SongAdapter()
        linearLayoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = songAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

        return view
    }
}