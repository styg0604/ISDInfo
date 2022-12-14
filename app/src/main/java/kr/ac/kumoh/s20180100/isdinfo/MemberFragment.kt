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

class MemberFragment : Fragment() {
    private lateinit var memberModel: MemberViewModel

    // 레이아웃 매니저 및 어댑터 전역변수 선언
    private var linearLayoutManager: RecyclerView.LayoutManager? = null
    private var memberAdapter: RecyclerView.Adapter<MemberAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        memberModel = ViewModelProvider(this)[MemberViewModel::class.java]

        memberModel.list.observe(this) {
            memberAdapter?.notifyItemRangeInserted(0,
                memberModel.list.value?.size ?: 0)
        }

        memberModel.requestMember()
    }

    inner class MemberAdapter : RecyclerView.Adapter<MemberAdapter.ViewHolder>(){
        //inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        inner class ViewHolder(itemView: View)
            : RecyclerView.ViewHolder(itemView), View.OnClickListener {

            // RecyclerView에 ViewHolder 설정
            val memName: TextView = itemView.findViewById(R.id.member_name)
            val fanName: TextView = itemView.findViewById(R.id.fandom_name)
            val memImage: NetworkImageView = itemView.findViewById(R.id.member_image)

            init {
                // 기본 이미지 설정
                memImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                // 클릭 이벤트 리스터 초기화
                itemView.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                // 아이템 클릭 시 MemberActicity로 데이터 전달 및 이동
                val intent = Intent(context, MemberActivity::class.java)
                intent.putExtra(MemberActivity.KEY_NAME,
                    memberModel.list.value?.get(adapterPosition)?.name)
                intent.putExtra(MemberActivity.KEY_HEIGHT,
                    memberModel.list.value?.get(adapterPosition)?.height)
                intent.putExtra(MemberActivity.KEY_BIRTH,
                    memberModel.list.value?.get(adapterPosition)?.birth)
                intent.putExtra(MemberActivity.KEY_MBTI,
                    memberModel.list.value?.get(adapterPosition)?.mbti)
                intent.putExtra(MemberActivity.KEY_YT_LINK,
                    memberModel.list.value?.get(adapterPosition)?.youtubeLink)
                intent.putExtra(MemberActivity.KEY_TW_LINK,
                    memberModel.list.value?.get(adapterPosition)?.twitchLink)
                intent.putExtra(MemberActivity.KEY_DESCRIPTION,
                    memberModel.list.value?.get(adapterPosition)?.description)
                intent.putExtra(MemberActivity.KEY_FAN_NAME,
                    memberModel.list.value?.get(adapterPosition)?.fanName)
                intent.putExtra(MemberActivity.KEY_FAN_DESCRIPTION,
                    memberModel.list.value?.get(adapterPosition)?.fanDescription)
                intent.putExtra(MemberActivity.KEY_IMAGE,
                    memberModel.getImageUrl(adapterPosition))
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.item_member,
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.memName.text = memberModel.list.value?.get(position)?.name
            holder.fanName.text = memberModel.list.value?.get(position)?.fanName
            holder.memImage.setImageUrl(memberModel.getImageUrl(position), memberModel.imageLoader)
        }

        override fun getItemCount() = memberModel.list.value?.size ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // 레이아웃 연결
        val view = inflater.inflate(R.layout.fragment_member, container, false)
        
        // RecyclerView를 변수에 저장 
        val recyclerView: RecyclerView = view.findViewById(R.id.member_list)

        // 어댑터 및 레이아웃 매니저 초기화
        memberAdapter = MemberAdapter()
        linearLayoutManager = LinearLayoutManager(activity)

        // RecyclerView 설정
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = memberAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)

        return view
    }
}