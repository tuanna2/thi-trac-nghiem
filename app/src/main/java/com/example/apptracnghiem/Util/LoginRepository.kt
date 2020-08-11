package com.example.apptracnghiem.Util

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.apptracnghiem.Data.LogInResponse
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class LoginRepository(val context:Context) {
    val TAG = "LoginRequestTAG"

    private val requestQueue = Volley.newRequestQueue(context)
    private val url = Server.logIn()
    private val url2 =Server.signUp()
    private val gson = Gson()

    fun logIn(email:String, password:String,callback: OperationCallback){
        val jsonObject= JSONObject()
        jsonObject.put("email",email)
        jsonObject.put("password",password)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            Response.Listener {response ->  
                Log.v("CONSOLE",response.toString())
                var jsonObject:JSONObject?=null
                try {
                    jsonObject = JSONObject(response.toString())
                    val loginResponse: LogInResponse = gson.fromJson(
                        jsonObject.toString(),
                        LogInResponse::class.java
                    )
                    if(loginResponse.isSuccess()){
                        callback.onSuccess(loginResponse.data)
                    }else{
                        callback.onError(loginResponse.message)
                    }

                }catch (e:JSONException){
                    callback.onError(e.message)
                }
            },
            Response.ErrorListener {error ->  
                Log.v("CONSOLE","message ${error.message} responseError ${error.networkResponse.statusCode}")
                val messageError= "error : ${error.networkResponse.statusCode} ".plus("message ${error.message}")
                callback.onError(messageError)
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun signUp(username:String,email: String,password: String,callback: OperationCallback){
        val jsonObject= JSONObject()
        jsonObject.put("username",username)
        jsonObject.put("email",email)
        jsonObject.put("password",password)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,url2,jsonObject,
            Response.Listener {response ->
                Log.v("CONSOLE",response.toString())
                var jsonObject:JSONObject?=null
                try {
                    jsonObject = JSONObject(response.toString())
                    val loginResponse: LogInResponse = gson.fromJson(
                        jsonObject.toString(),
                        LogInResponse::class.java
                    )
                    if(loginResponse.isSuccess()){
                        callback.onSuccess(loginResponse.data)
                    }else{
                        callback.onError(loginResponse.message)
                    }

                }catch (e:JSONException){
                    callback.onError(e.message)
                }
            },
            Response.ErrorListener {error ->
                Log.v("CONSOLE","message ${error.message} responseError ${error.networkResponse.statusCode}")
                val messageError= "error : ${error.networkResponse.statusCode} ".plus("message ${error.message}")
                callback.onError(messageError)
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun cancelOperation(){
        requestQueue?.cancelAll(TAG)
    }

    companion object {

        private var INSTANCE: LoginRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [TasksRepository] instance
         */
        @JvmStatic fun getInstance(context: Context): LoginRepository {
            return INSTANCE ?: LoginRepository(context)
                .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}