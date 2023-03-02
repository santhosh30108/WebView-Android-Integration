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

    val ace_token = "52ad105b-624a-4dab-8085-5bea78ec6126"
    val refresh_token = "c136803e-e798-4a2e-9e5d-fe500895efe5"
//    val url = "http://10.0.2.2:3000/lms/survey/a7c68e71-fa77-468a-b37e-0669a73d4f08/?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwNDA0NzMzNiIsImRhdGEiOiJ7XCJwc2lkXCI6XCIwMDAwNDA0NzMzNlwifSIsImV4cCI6MTc3NzA1Njc0NiwiaWF0IjoxNjc3MDU2NzQ2fQ.uJoWq8jW41VF0NzrYmMkiu9D_AJvsZF8C_xMtUJvtAxm5eiSKMQGknkc9LQ6ZECwD2nT9K0Y3U04BpPvzOAk-Q&utm_source=sms"
    val url = "http://10.0.2.2:3000/lms/webview-pages/fee-payment/?ace_token="+ace_token+"&refresh_token="+refresh_token
    lateinit var Type1 : String
    lateinit var Type2 : String
    lateinit var prePayload64:String
    lateinit var postPayload64: String

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        yes = findViewById(R.id.button)
        no = findViewById(R.id.button2)
        webView.settings.javaScriptEnabled = true;
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(JSBridge(), "jsObject")
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView = findViewById(R.id.webView)
        WebView.setWebContentsDebuggingEnabled(true)

        val preOrderPayload = responseObject(
            "Medhya Saruk",
            "monikafotedar@yahoo.com",
            "16896", "Two Years Integrated Course for NEET - July Batches",
            "742612", "6", "2022-05-24T18:08:35.319014Z",
            "2022", "BI040", "300000000601-5196,300000000602-7020,300000000603-4680"
        )

        val postOrderPayload = authObject(
            ace_token, refresh_token
        )

        Type1 = "SEND_PREORDER_API_PARAMS"
        Type2 = "SEND_POSTORDER_API_PARAMS"

        val preJsonString = Gson().toJson(preOrderPayload)
        val postJsonString = Gson().toJson(postOrderPayload)

        prePayload64 = Base64.encodeToString(preJsonString.toByteArray(), Base64.DEFAULT)
            .replace("\\s".toRegex(), "")

        postPayload64 = Base64.encodeToString(postJsonString.toByteArray(), Base64.DEFAULT)
            .replace("\\s".toRegex(), "")

        yes.setOnClickListener {
            webView.loadUrl(url)
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
            "TRANSACTION_PAGE_RENDERED" ->{
                if (payloadData.pre_transaction){
                    Log.d("payload", prePayload64.trim() + "abc")
                    runOnUiThread {
                        webView.evaluateJavascript(
                            "javascript: " + "sendEventToWeb(\"" + Type1 +
                                    "\",\"" + prePayload64.trim() + "\")", null
                        )
                }
                }
                else {
                    Log.d("payload", postPayload64.trim() + "abc")
                    runOnUiThread {
                        webView.evaluateJavascript(
                            "javascript: " + "sendEventToWeb(\"" + Type2 +
                                    "\",\"" + postPayload64.trim() + "\")", null
                        )
                    }
                }
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

class responseObject(name:String,email:String,amount:String,courseName:String,courseId:String,installmentNumber:String,dueDate:String,term:String,businessUnit:String,itemTypes:String){
    val name = name
    val email = email
    val amount = amount
    val courseName = courseName
    val courseId = courseId
    val installmentNumber = installmentNumber
    val dueDate = dueDate
    val term = term
    val businessUnit = businessUnit
    val itemTypes = itemTypes
}

class authObject(ace_token:String,refresh_token:String){
    val ace_token = ace_token
    val refresh_token = refresh_token
}
