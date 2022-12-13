package kr.ac.kumoh.s20180100.isdinfo

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.collection.LruCache
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class MemberViewModel(application: Application) : AndroidViewModel(application) {
    data class Member (
        var id: Int, var name: String, var height: String, var birth: String,
        var mbti: String, var youtubeLink: String, var twitchLink: String,
        var description: String, var image: String,
        var fanName: String, var fanDescription: String
    )

    companion object{
        const val QUEUE_TAG = "MemberVolleyRequest"
        const val SERVER_URL = "https://isd-ycvnu.run.goorm.io"
    }

    private val members = ArrayList<Member>()
    private val _list = MutableLiveData<ArrayList<Member>>()
    val list: LiveData<ArrayList<Member>>
        get() = _list

    private val queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = members
        queue = Volley.newRequestQueue(getApplication())
        imageLoader = ImageLoader(queue,
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
    }

    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(members[i].image, "utf-8")

    fun requestMember(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            "$SERVER_URL/member",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                members.clear()
                parseJson(it)
                _list.value = members
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item:JSONObject = items[i] as JSONObject
            val id = item.getInt("m_id")
            val name = item.getString("m_name")
            val height = item.getString("m_height")
            val birth = item.getString("m_birth")
            val mbti = item.getString("m_mbti")
            val youtubeLink = item.getString("ytb_link")
            val twitchLink = item.getString("twch_link")
            val description = item.getString("m_description")
            val image = item.getString("m_image")
            val fanName = item.getString("f_name")
            val fanDescription = item.getString("f_description")

            members.add(
                Member(
                    id, name, height, birth, mbti, youtubeLink, twitchLink,
                    description, image, fanName, fanDescription
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}