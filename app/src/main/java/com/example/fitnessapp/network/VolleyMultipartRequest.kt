package com.example.fitnessapp.network

import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, errorListener) {

    private val boundary = "apiclient-${System.currentTimeMillis()}"
    private val mimeType = "multipart/form-data;boundary=$boundary"

    override fun getBodyContentType(): String = mimeType

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        val bos = ByteArrayOutputStream()
        val params = getParams() ?: mapOf()
        val data = getByteData() ?: mapOf()
        try {
            // Text params
            for ((key, value) in params) {
                bos.write("--$boundary\r\n".toByteArray())
                bos.write("Content-Disposition: form-data; name=\"$key\"\r\n\r\n".toByteArray())
                bos.write(value.toByteArray())
                bos.write("\r\n".toByteArray())
            }
            // File params
            for ((key, dataPart) in data) {
                bos.write("--$boundary\r\n".toByteArray())
                bos.write("Content-Disposition: form-data; name=\"$key\"; filename=\"${dataPart.fileName}\"\r\n".toByteArray())
                bos.write("Content-Type: ${dataPart.type}\r\n\r\n".toByteArray())
                bos.write(dataPart.content)
                bos.write("\r\n".toByteArray())
            }
            bos.write("--$boundary--\r\n".toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bos.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return try {
            Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse) {
        listener.onResponse(response)
    }

    open fun getByteData(): Map<String, DataPart>? = null

    data class DataPart(val fileName: String, val content: ByteArray, val type: String)
}
