package com.example.datastorepoc.pref

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.datastorepoc.OnClickHandlerInterface
import com.example.datastorepoc.R
import com.example.datastorepoc.databinding.ActivityDatastoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrefDataStoreActivity : AppCompatActivity(), OnClickHandlerInterface {

    private lateinit var prefSettingsManager: PrefSettingsManager
    private lateinit var binding: ActivityDatastoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_datastore)

        binding.clickHandler = this

        prefSettingsManager= PrefSettingsManager(this)
        readSettings()
    }


    private fun updateSettings(color:Int){
        lifecycleScope.launch {
            prefSettingsManager.updateColor(color)
        }
    }

    private fun readSettings() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                prefSettingsManager.userPreferencesFlow.collect {
                    binding.outerView.setBackgroundColor(
                        ContextCompat.getColor(
                            this@PrefDataStoreActivity,
                            it.color
                        )
                    )
                }
            }
        }
    }

    override fun onClick(view: View) {
        when(view){
            binding.btnWhite->updateSettings(android.R.color.white)
            binding.btnBlack->updateSettings(android.R.color.black)
        }

    }
}