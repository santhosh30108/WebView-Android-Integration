package com.byjus.acefe

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var yes: Button
    lateinit var no:Button

    val doubtId = "4e19822b-0547-4a8e-b356-758fa2241e20"
    val token = "ccd5ca8a-c326-4249-be7d-1da8c3fb22f8"
    val refreshtoken = "e86d0852-cca4-4546-9c42-ff0ae9705674"
    val askAnExpert = false
    val url = "http://10.0.2.2:3000/lms/webview-pages/ask-tutor/doubt/?doubtId="+doubtId+"&token="+token+"&refreshToken="+refreshtoken
//    val url = "http://10.0.2.2:3000/lms/webview-pages/ask-tutor/doubt/?doubtId=1ad70016-187c-4a3a-afa9-40e675308279"
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        yes = findViewById(R.id.button)
        no = findViewById(R.id.button2)
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true;
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(JSBridge(),"jsObject")
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView = findViewById(R.id.webView)
//    webView.setWebContentsDebuggingEnabled(true)

        val requestFeeback = RequestFeeBack("3","Solved")
        val jsonString = Gson().toJson(requestFeeback)

        val Type:String = "IS_YOUR_DOUBT_SOLVED"
        val yesPayload = "yes"
        val noPayload = "load_more"

    val nopayload64 = Base64.encodeToString(noPayload.toByteArray(), Base64.DEFAULT)
    val yespayload64 = Base64.encodeToString(yesPayload.toByteArray(), Base64.DEFAULT)
//    val payload64: String = URLEncoder.encode(mbPayload, "UTF-8")

        yes.setOnClickListener{
            Log.d("payload",yespayload64.trim()+"abc")
//            webView.evaluateJavascript("sendEventToWeb($mbType, $mbPayload)", null)
            webView.evaluateJavascript(
                "javascript: " +"sendEventToWeb(\"" + Type +
                        "\",\""+yespayload64.trim()+"\")",null)
        }
    no.setOnClickListener{
        Log.d("payload",nopayload64.trim()+"abc")
//            webView.evaluateJavascript("sendEventToWeb($mbType, $mbPayload)", null)
        webView.evaluateJavascript(
            "javascript: " +"sendEventToWeb(\"" + Type +
                    "\",\""+nopayload64.trim()+"\")",null)
    }
    }


    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    fun showMessage(type: String, payload: String) {

        val payloadData: PayloadData = Gson().fromJson(payload, PayloadData::class.java)
        when (type) {
            "SHOW_SOLUTION_FEEDBACK" -> {
                Toast.makeText(this, type+" received", Toast.LENGTH_SHORT).show()
            }
            "REQUEST_FEEDBACK" -> {
                Toast.makeText(this, type+"   "+payloadData.data, Toast.LENGTH_SHORT).show()
            }
            "ASK_EXPERT" ->{
                Toast.makeText(this, type+" received", Toast.LENGTH_SHORT).show()
            }
            "LOADING" ->{
                Toast.makeText(this, "Loading - "+payloadData.status, Toast.LENGTH_SHORT).show()
            }

        }

    }

    inner class JSBridge{

        @JavascriptInterface

        fun sendEventToNative(TYPE: String,PAYLOAD:String){
            val payload = String(Base64.decode(PAYLOAD, Base64.DEFAULT))
            Log.d("msg123",TYPE+payload)

            showMessage(TYPE,payload)
        }


    }

}

class RequestFeeBack(rating:String,remarks:String){
    val rating = rating
    val remarks = remarks
}