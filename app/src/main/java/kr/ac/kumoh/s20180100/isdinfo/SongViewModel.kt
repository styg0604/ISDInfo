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

class SongViewModel(application: Application) : AndroidViewModel(application) {
    data class Song (
        var id: Int, var title: String, var type: String, var upload: String,
        var image: String, var link: String, var description: String
    )

    companion object{
        const val QUEUE_TAG = "SongVolleyRequest"
        const val SERVER_URL = "https://isd-ycvnu.run.goorm.io"
    }

    private val songs = ArrayList<Song>()
    private val _list = MutableLiveData<ArrayList<Song>>()
    val list: LiveData<ArrayList<Song>>
        get() = _list

    private val queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        // 리스트, 큐, 이미지로더 초기화
        _list.value = songs
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

    /** 서버에서 가져온 이미지 주소 반환 */
    fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(songs[i].image, "utf-8")

    fun requestSong(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            "$SERVER_URL/song",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                // 서버와 통신이 성공하면 리스트에 저장
                songs.clear()
                parseJson(it)
                _list.value = songs
            },
            {
                // 서버와 통신이 실패하면 Toast로 오류 메시지 출력
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )

        // 큐에 request 저장
        request.tag = QUEUE_TAG
        queue.add(request)
    }

    /** JSON 배열에서 아이템을 추출하여 저장 */
    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item:JSONObject = items[i] as JSONObject
            val id = item.getInt("s_id")
            val title = item.getString("s_title")
            val type = item.getString("s_type")
            val upload = item.getString("s_upload")
            val image = item.getString("s_image")
            val link = item.getString("s_link")
            val description = item.getString("s_description")

            songs.add(Song(id, title, type, upload, image, link, description))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}