package top.fumiama.runestrans

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.endrnd.*
import kotlinx.android.synthetic.main.endrnd.view.*
import kotlinx.android.synthetic.main.startrnd.view.*


class MainActivity : Activity() {
    var firstItem = true
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListenerToRootView()
        yaru.setOnClickListener {
            if(inptxt.text.isNotEmpty()){
                val chat = layoutInflater.inflate(R.layout.startrnd, ln, false)
                val rchat = layoutInflater.inflate(R.layout.endrnd, ln, false)
                chat.tl.text = turn(inptxt.text.toString())
                rchat.tr.text = inptxt.text
                inptxt.text.clear()
                if(ln.childCount > 0) firstItem = false
                ln.addView(
                    rchat, LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                ln.addView(TextView(this))
                ln.addView(
                    chat, LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                ln.addView(TextView(this))
                setColor(chat.cl)
                setColor(rchat.cr)
                scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN); inptxt.requestFocus(); }
                if(!firstItem && scroll.translationY == 0f) scroll.upOverScroll()
            }
        }
        yaru.setOnLongClickListener {
            firstItem = true
            for(i in ln.children.iterator()){
                ObjectAnimator.ofFloat(i, "alpha", 1F, 0F).setDuration(
                    111 + ln.indexOfChild(i).toLong() * 10
                ).start()
                ObjectAnimator.ofFloat(
                    i,
                    "translationX",
                    i.translationX,
                    if (ln.indexOfChild(i) % 4 != 0) -i.width.toFloat() else i.width.toFloat()
                ).setDuration(111 + ln.indexOfChild(i).toLong() * 10).start()
            }
            ln.postDelayed({ ln.removeAllViews() }, 111 + ln.childCount.toLong() * 10)
            true
        }
    }
    private fun setColor(v: CardView){
        when((0..5).random()){
            0 -> v.setCardBackgroundColor(resources.getColor(R.color.colora, theme))
            1 -> v.setCardBackgroundColor(resources.getColor(R.color.colorb, theme))
            2 -> v.setCardBackgroundColor(resources.getColor(R.color.colorc, theme))
            3 -> v.setCardBackgroundColor(resources.getColor(R.color.colord, theme))
            4 -> v.setCardBackgroundColor(resources.getColor(R.color.colore, theme))
            5 -> v.setCardBackgroundColor(resources.getColor(R.color.colorf, theme))
        }
    }
    private fun setListenerToRootView() {
        val rootView: View = window.decorView.rootView
        val softKeyboardHeight = 100
        var heightDiff = 0
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val dm: DisplayMetrics = rootView.resources.displayMetrics
            val hd: Int = rootView.bottom - r.bottom
            if(hd != heightDiff){
                heightDiff = hd
                val isKeyboardShown = heightDiff > softKeyboardHeight * dm.density
                if (isKeyboardShown) {
                    //Toast.makeText(this, "弹出$heightDiff", Toast.LENGTH_SHORT).show()
                    if(!firstItem || scroll.getChildAt(0).height > rootView.height) scroll.upOverScroll()
                    inptxt.requestFocus()
                } else {
                    //Toast.makeText(this, "收回$heightDiff", Toast.LENGTH_SHORT).show()
                    if(!firstItem || scroll.getChildAt(0).height > rootView.height) scroll.downToNormal()
                }
            }
            if(scroll.maxOverScrollY == 0) scroll.maxOverScrollY = yaru.height / 64 * 150
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun turn(inpstr: String): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("rune")
        }
    }
}
