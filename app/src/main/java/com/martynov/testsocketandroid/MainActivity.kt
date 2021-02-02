package com.martynov.testsocketandroid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mBtnOpen: Button? = null
    private var mBtnSend: Button? = null
    private var mBtnClose: Button? = null
    private var mEdit: EditText? = null
    private var mConnect: Connection? = null
    private var textView: TextView? = null
    val LOG_TAG = "My"

    private val HOST = "93.179.85.126"
    private val PORT = 5550

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnOpen = findViewById<Button>(R.id.btn_open)
        mBtnSend = findViewById<Button>(R.id.btn_send)
        mBtnClose = findViewById<Button>(R.id.btn_close)
        mEdit = findViewById<EditText>(R.id.edText)
        textView = findViewById<TextView>(R.id.textView)
//        mBtnSend?.setEnabled(false)
//        mBtnClose?.setEnabled(false)


        mBtnOpen?.setOnClickListener(View.OnClickListener { onOpenClick() })

        mBtnSend?.setOnClickListener(View.OnClickListener { onSendClick() })

        mBtnClose?.setOnClickListener(View.OnClickListener { onCloseClick() })


        Thread {
            while (true) {
                Log.d("My", "Зашел")
                var str = mConnect?.outData()
                Log.d("My", str.toString())
                if (str.equals(null)) {

                } else {
                    runOnUiThread {
                        val strEnd = "${textView?.text} \n $str"
                        textView?.text = strEnd
                    }
                }

            }
        }.start()
        Thread {
            try {


                mConnect = Connection(HOST, PORT)
                // Разблокирование кнопок в UI потоке
                Log.d(LOG_TAG, "Соединение установлено")
                Log.d(LOG_TAG, "(mConnect != null) = " + (mConnect != null))
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message!!)
                mConnect = null
            }
        }.start()

    }


    private fun onOpenClick() {

    }

    private fun onSendClick() {
        if (mConnect == null) {
            Log.d(LOG_TAG, "Соединение не установлено")
        } else {
            Log.d(LOG_TAG, "Отправка сообщения")
            Thread {
                try {
                    var text = mEdit?.text.toString()
                    Log.d(LOG_TAG, text)
                    /* отправляем на сервер данные */
                    mConnect?.sendData(text)
                } catch (e: java.lang.Exception) {
                    Log.e(LOG_TAG, e.message!!)
                }
            }.start()
        }
    }

    private fun onCloseClick() {
        // Закрытие соединения
        mConnect!!.closeConnection()
        // Блокирование кнопок
        mBtnSend!!.isEnabled = false
        mBtnClose!!.isEnabled = false
        Log.d(LOG_TAG, "Соединение закрыто")
    }


}