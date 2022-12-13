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
        const val KEY_NAME = "MemberName"
        const val KEY_HEIGHT = "MemberHeight"
        const val KEY_BIRTH = "MemberBirth"
        const val KEY_MBTI = "MemberMBTI"
        const val KEY_YT_LINK = "MemberYoutubeLink"
        const val KEY_TW_LINK = "MemberTwitchLink"
        const val KEY_DESCRIPTION = "MemberDescription"
        const val KEY_IMAGE = "MemberImage"
        const val KEY_FAN_NAME = "MemberFanName"
        const val KEY_FAN_DESCRIPTION = "MemberFanDescription"
    }

    private lateinit var binding: ActivityMemberBinding
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
        binding.btnTwitch.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twLink))
            startActivity(intent)
        }

        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFFDDA0DD.toInt()))
        supportActionBar?.title = binding.textName.text
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