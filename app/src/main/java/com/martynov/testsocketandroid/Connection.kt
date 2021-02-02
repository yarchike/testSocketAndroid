package com.martynov.testsocketandroid

import android.util.Log
import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class Connection(val mHost: String, val mPort: Int = 0) {
    private var mSocket: Socket? = Socket(mHost, mPort).apply {

    }
    val LOG_TAG = "SOCKET"

    companion object {
        var inputUser: BufferedReader? = null
        var inServer: BufferedReader? = null
        var outServer: BufferedWriter? = null
        var userName: String? = null
        var time: Date? = null
        var dTime: String? = null
        var dt1: SimpleDateFormat? = null
    }

    init {
        try {
            inputUser = BufferedReader(InputStreamReader(System.`in`))
            inServer = BufferedReader(InputStreamReader(mSocket?.getInputStream()))
            outServer = BufferedWriter(OutputStreamWriter(mSocket?.getOutputStream()))
        } catch (e: IOException) {
            downService()
        }
    }

    fun downService() {
        try {
            if (!mSocket!!.isClosed) {
                mSocket?.close()
                inServer?.close()
                outServer?.close()
            }
        } catch (ignored: IOException) {
        }
    }


    fun closeConnection() {

        /* Проверяем сокет. Если он не зарыт, то закрываем его и освобдождаем соединение.*/if (mSocket != null && !mSocket!!.isClosed) {
            try {
                mSocket!!.close()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Невозможно закрыть сокет: " + e.message)
            } finally {
                mSocket = null
            }
        }
        mSocket = null
    }

    fun sendData(data: String) {

        // Проверка открытия сокета
        if (mSocket == null || mSocket!!.isClosed) {
            throw java.lang.Exception("Невозможно отправить данные. Сокет не создан или закрыт")
        }
        // Отправка данных
        // Отправка данных
        try {
            time = Date()
            dt1 = SimpleDateFormat("HH:mm:ss")
            dTime = dt1?.format(time)
            userName = "Yarchik"
            outServer?.write("(" + dTime + ") " + userName + ": " + data + "\n")
            outServer?.flush()
        } catch (e: IOException) {
            downService()
        }

    }
    fun outData(): String?{
        var str: String? = null
        try{
                str = inServer?.readLine()
                println(str);

        }catch (ignored: IOException) {
            downService()
        }
        return str
    }
}