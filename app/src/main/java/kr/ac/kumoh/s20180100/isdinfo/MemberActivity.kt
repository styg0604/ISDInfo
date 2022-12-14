package kr.ac.kumoh.s20180100.isdinfo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20180100.isdinfo.databinding.ActivityMemberBinding

class MemberActivity : AppCompatActivity(){
    companion object {
        const val KEY_NAME = "MemberName" // 멤버 이름
        const val KEY_HEIGHT = "MemberHeight" // 멤버 키
        const val KEY_BIRTH = "MemberBirth" // 멤버 생일
        const val KEY_MBTI = "MemberMBTI" // 멤버 MBTI
        const val KEY_YT_LINK = "MemberYoutubeLink" // 멤버 유튜브 링크
        const val KEY_TW_LINK = "MemberTwitchLink" // 멤버 트위치 링크
        const val KEY_DESCRIPTION = "MemberDescription" // 멤버 설명
        const val KEY_IMAGE = "MemberImage" // 멤버 이미지
        const val KEY_FAN_NAME = "MemberFanName" // 멤버 팬덤 이름
        const val KEY_FAN_DESCRIPTION = "MemberFanDescription" // 멤버 팬덤 설명
    }

    private lateinit var binding: ActivityMemberBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이미지 로더 => 캐시 설정
        imageLoader = ImageLoader(Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            }
        )

        // 전달된 데이터 저장
        binding.textName.text = intent.getStringExtra(KEY_NAME)
        binding.textHeight.text = intent.getStringExtra(KEY_HEIGHT)
        binding.textBirth.text = intent.getStringExtra(KEY_BIRTH)
        binding.textMbti.text = intent.getStringExtra(KEY_MBTI)
        binding.textDescription.text = intent.getStringExtra(KEY_DESCRIPTION)
        binding.textFanName.text = intent.getStringExtra(KEY_FAN_NAME)
        binding.textFanDescription.text = intent.getStringExtra(KEY_FAN_DESCRIPTION)

        binding.imageMember.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)

        val ytLink = intent.getStringExtra(KEY_YT_LINK)
        val twLink = intent.getStringExtra(KEY_TW_LINK)

        // 유튜브 및 트위치 버튼 클릭시 이벤트 리스터
        // 해당 링크로 이동
        binding.btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
        binding.btnTwitch.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twLink))
            startActivity(intent)
        }

        // 상단 액션 바 배경 색과 텍스트 설정
        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFFDDA0DD.toInt()))
        supportActionBar?.title = binding.textName.text

        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}