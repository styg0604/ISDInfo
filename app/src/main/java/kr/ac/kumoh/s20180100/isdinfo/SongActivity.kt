package kr.ac.kumoh.s20180100.isdinfo

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.ac.kumoh.s20180100.isdinfo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(){
    companion object {
        const val KEY_TITLE = "SongTitle"
        const val KEY_TYPE = "SongType"
        const val KEY_UPLOAD = "SongUpload"
        const val KEY_IMAGE = "SongImage"
        const val KEY_LINK = "SongLink"
        const val KEY_DESCRIPTION = "SongDescription"
    }

    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTitle.text = intent.getStringExtra(KEY_TITLE)
        binding.textType.text = intent.getStringExtra(KEY_TYPE)
        binding.textUpload.text = intent.getStringExtra(KEY_UPLOAD)
        binding.textDescription.text = intent.getStringExtra(KEY_DESCRIPTION)

        var link = intent.getStringExtra(KEY_LINK).toString()

        if (link == "null") {
            link = "dQw4w9WgXcQ"
            binding.textPlease.text = "영상이 없어요!"
        }
        binding.ytSong.play(link)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFFDDA0DD.toInt()))
        supportActionBar?.title = binding.textTitle.text
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