package kr.ac.kumoh.s20180100.isdinfo

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kr.ac.kumoh.s20180100.isdinfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        /** Tag로 Fragment 구분 => SongFragment() */
        const val TAG_SONG = "song_fragment"
        /** Tag로 Fragment 구분 => MemberFragment() */
        const val TAG_MEMBER = "member_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액션 바 색깔과 텍스트 지정
        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFFDDA0DD.toInt()))
        supportActionBar?.title = "이세계 아이돌"

        // 안드로이드 네비게이션 바의 색깔 지정
        window.navigationBarColor = resources.getColor(R.color.pink)

        // 초기 Fragment 설정
        setFragment(TAG_SONG, SongFragment())

        // 하단 네비게이션 메뉴의 아이템에 따른 Fragment 설정
        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.songFragment -> setFragment(TAG_SONG, SongFragment())
                R.id.memberFragment -> setFragment(TAG_MEMBER, MemberFragment())
            }
            true
        }
    }

    /** 화면에 출력될 Fragment 설정 함수 */
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val song = manager.findFragmentByTag(TAG_SONG)
        val member = manager.findFragmentByTag(TAG_MEMBER)

        if (song != null) {
            fragTransaction.hide(song)
        }

        if (member != null) {
            fragTransaction.hide(member)
        }

        if (tag == TAG_SONG) {
            if (song!=null){
                fragTransaction.show(song)
            }
        }
        else if (tag == TAG_MEMBER) {
            if (member != null) {
                fragTransaction.show(member)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}