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

    // 레이아웃 매니저 및 어댑터 전역변수 선언
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

            // RecyclerView에 ViewHolder 설정
            val txTitle: TextView = itemView.findViewById(R.id.text1)
            val txType: TextView = itemView.findViewById(R.id.text2)
            val niImage: NetworkImageView = itemView.findViewById(R.id.image)

            init {
                // 기본 이미지 설정
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                // 클릭 이벤트 리스터 초기화
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                // 아이템 클릭 시 MemberActicity로 데이터 전달 및 이동
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

        // 레이아웃 연결
        val view = inflater.inflate(R.layout.fragment_song, container, false)

        // RecyclerView를 변수에 저장
        val recyclerView: RecyclerView = view.findViewById(R.id.song_list)

        // 어댑터 및 레이아웃 매니저 초기화
        songAdapter = SongAdapter()
        linearLayoutManager = LinearLayoutManager(activity)

        // RecyclerView 설정
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = songAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

        return view
    }
}