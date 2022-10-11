package com.byjus.acefe

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var textView: TextView
    lateinit var editText: EditText
    lateinit var button: Button
//    val url = "http://10.0.2.2:8000/out/doc/"
    val url = "http://10.0.2.2:9000"

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textWebView)
        editText = findViewById(R.id.textAndroidView)
        button = findViewById(R.id.androidbutton)
        webView = findViewById(R.id.webView)
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true;
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(JSBridge(),"bridge")

        button.setOnClickListener{
            webView.evaluateJavascript(
                "javascript: " +"dataToWeb(\"" + editText.text +
                    "\")",null)
        }
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    fun showMessage(message: String){
        textView.text = message
    }

     inner class JSBridge{

        @JavascriptInterface

        fun clickHandler(message: String){
            Log.d("msg",message)
            showMessage(message)
        }

    }

    }

//        {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                injectedJavascript(view)
//
//            }
//        }

//    private fun injectedJavascript(view: WebView?) {
//        view?.loadUrl(
//    """javascript:
//                let button = document.getElementById('webButton');
//                let header = document.getElementById("header");
//                button.addEventListener("click",function(){
//                header.innerText = "Header Changed";
//                bridge.clickHandler();
//                });
//                """
//        );
//    }
//    """javascript:
//                window.buttonClicked = false;
//                if(window.buttonClicked === true){
//                    bridge.clickHandler();
//                }
//              """

//    """javascript:
//                let button = document.getElementById('webButton');
//                button.addEventListener("click",function(){
//                    clickHandler(JSBridge.clickHandler)
//                });
//                """


//    """javascript:
//                let button = document.getElementById('webButton');
//                let header = document.getElementById("header");
//                button.addEventListener("click",function(){
//                header.innerText = "Header Changed";
//                bridge.clickHandler();
//                });
//                """
