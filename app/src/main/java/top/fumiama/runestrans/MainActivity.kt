package top.fumiama.runestrans

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.endrnd.view.*
import kotlinx.android.synthetic.main.startrnd.view.*

class MainActivity : Activity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        yaru.setOnClickListener {
            if(inptxt.text.isNotEmpty()){
                val chat = layoutInflater.inflate(R.layout.startrnd, ln, false)
                val rchat = layoutInflater.inflate(R.layout.endrnd, ln, false)
                chat.tl.text = turn(inptxt.text.toString())
                rchat.tr.text = inptxt.text
                inptxt.text.clear()
                ln.addView(rchat, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                ln.addView(TextView(this))
                ln.addView(chat, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                ln.addView(TextView(this))
                setColor(chat.cl)
                setColor(rchat.cr)
                scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN); inptxt.requestFocus(); }
            }
        }
        yaru.setOnLongClickListener {
            for(i in ln.children.iterator()){
                ObjectAnimator.ofFloat(i, "alpha", 1F, 0F).setDuration(111 + ln.indexOfChild(i).toLong() * 10).start()
                ObjectAnimator.ofFloat(i, "translationX", i.translationX, if(ln.indexOfChild(i) % 4 != 0) -i.width.toFloat() else i.width.toFloat()).setDuration(111 + ln.indexOfChild(i).toLong() * 10).start()
            }
            ln.postDelayed({ln.removeAllViews()}, 111 + ln.childCount.toLong() * 10)
            true
        }
        inptxt.setOnClickListener { scroll.postDelayed({scroll.fullScroll(ScrollView.FOCUS_DOWN); inptxt.requestFocus();}, 233) }
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
