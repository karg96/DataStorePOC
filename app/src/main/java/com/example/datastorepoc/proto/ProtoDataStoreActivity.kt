package com.example.datastorepoc.proto

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.datastorepoc.OnClickHandlerInterface
import com.example.datastorepoc.R
import com.example.datastorepoc.databinding.ActivityDatastoreBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProtoDataStoreActivity : AppCompatActivity(), OnClickHandlerInterface {

    private lateinit var protoSettingsManager: ProtoSettingsManager
    private lateinit var binding: ActivityDatastoreBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_datastore)
        binding.clickHandler = this

        protoSettingsManager = ProtoSettingsManager(this)

        readSettings()

    }

    private fun readSettings() {
        lifecycleScope.launch {

            //      withContext(Dispatchers.Main){
            protoSettingsManager.userSettingsFlow.collect {

                when (it.bgColor) {
                    UserInfo.UserSettings.BgColor.COLORWHITE -> binding.outerView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ProtoDataStoreActivity,
                            android.R.color.white
                        )
                    )
                    UserInfo.UserSettings.BgColor.COLORBLACK -> binding.outerView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@ProtoDataStoreActivity,
                            android.R.color.black
                        )
                    )
                    else -> {
                        // do nothing
                        Log.d("EMpty", "readSettings: ")
                    }
                }
                //}
            }


        }

    }

    private fun updateSettings(bgColor: UserInfo.UserSettings.BgColor) {
        lifecycleScope.launch {
            protoSettingsManager.updateColor(bgColor)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnWhite -> {
                updateSettings(UserInfo.UserSettings.BgColor.COLORWHITE)

            }
            binding.btnBlack -> updateSettings(UserInfo.UserSettings.BgColor.COLORBLACK)
        }
    }

}