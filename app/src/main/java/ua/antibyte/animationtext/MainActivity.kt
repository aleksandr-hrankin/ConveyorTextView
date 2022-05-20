package ua.antibyte.animationtext

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var index = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemProgressBar()
        setContentView(R.layout.activity_main)

        retry_button.setOnClickListener {
            index++
            if (index < getDataList().size) {
                text_anim_view.setText(getDataList()[index].text)
                text_anim_view.setImage(getDataList()[index].resId)
                text_anim_view.start()
            }
        }
    }

    private fun hideSystemProgressBar() {
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun getDataList() = listOf(
        Data("JUST DO IT.", R.drawable.icon_nike),
        Data("Don't be evil", R.drawable.icon_google),
        Data("Think different", R.drawable.icon_apple),
        Data("The Ultimate Driving Machine", R.drawable.icon_bmw),
        Data("I’m Lovin’ It", R.drawable.icon_mc_donalds)
    )

    data class Data(
        val text: String,
        val resId: Int
    )
}