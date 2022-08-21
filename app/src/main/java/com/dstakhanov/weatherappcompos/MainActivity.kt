package com.dstakhanov.weatherappcompos

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dstakhanov.weatherappcompos.ui.MainCard
import com.dstakhanov.weatherappcompos.ui.TabLayout
import com.dstakhanov.weatherappcompos.ui.theme.WeatherAppComposTheme
import org.json.JSONObject

const val API_KEY = "012c748818fd41bbbdf112239221805"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           WeatherAppComposTheme {
               getData("London", this)
               Image(
                   painter = painterResource(id = R.drawable.weather_bg),
                   contentDescription = "im1",
                   modifier = Modifier
                       .fillMaxSize()
                       .alpha(0.5f),
                   contentScale = ContentScale.Crop
               )
               Column {
                   MainCard()
                   TabLayout()
               }
           }
        }
    }
}

@Composable
fun GetTemperature(city: String, context: Context) {
    val state = remember {
        mutableStateOf("Unknown")
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Temp in $city = ${state.value} C")
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    getResult(city, state, context)
                }, modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Refresh")
            }
        }
    }

}

private fun getResult(city: String, state: MutableState<String>, context: Context) {
    val url = "https://api.weatherapi.com/v1/current.json" +
            "?key=$API_KEY" +
            "&q=$city" +
            "&aqi=no"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            state.value = obj.getJSONObject("current").getString("temp_c")
        },
        { error ->
            Log.d("MyLog", "Error $error" )
        }
    )
    queue.add(stringRequest)
}
private fun getData(city:String, context: Context){
    val url = "https://api.weatherapi.com/v1/current.json?key=$API_KEY"+
            "&q=$city"+
            "&days=" +
            "3" +
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url,
        {
            response ->
            Log.d("MyLog", "VolleyError: $response")
        },
        {
           Log.d("MyLog", "VolleyError: $it")
        }
    )
    queue.add(sRequest)
}
