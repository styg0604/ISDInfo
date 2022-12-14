package kr.ac.kumoh.s20180100.isdinfo

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.ac.kumoh.s20180100.isdinfo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(){
    companion object {
        const val KEY_TITLE = "SongTitle" // 노래 제목
        const val KEY_TYPE = "SongType" // 노래 종류
        const val KEY_UPLOAD = "SongUpload" // 노래 업로드 날짜
        const val KEY_IMAGE = "SongImage" // 노래 이미지
        const val KEY_LINK = "SongLink" // 노래 링크
        const val KEY_DESCRIPTION = "SongDescription" // 노래 설명
    }

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달된 데이터 저장
        binding.textTitle.text = intent.getStringExtra(KEY_TITLE)
        binding.textType.text = intent.getStringExtra(KEY_TYPE)
        binding.textUpload.text = intent.getStringExtra(KEY_UPLOAD)
        binding.textDescription.text = intent.getStringExtra(KEY_DESCRIPTION)

        var link = intent.getStringExtra(KEY_LINK).toString()

        // 영상이 없는 경우 다른 영상으로 대체
        if (link == "null") {
            link = "dQw4w9WgXcQ"
            binding.textPlease.text = "영상이 없어요!"
        }

        // 유튜브 영상 플레이어 활성화
        binding.ytSong.play(link)

        // 상단 액션 바 색 및 텍스트 설정
        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFFDDA0DD.toInt()))
        supportActionBar?.title = binding.textTitle.text

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