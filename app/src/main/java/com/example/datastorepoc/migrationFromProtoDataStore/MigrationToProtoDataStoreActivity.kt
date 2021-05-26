package com.example.datastorepoc.migrationFromProtoDataStore

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.datastorepoc.OnClickHandlerInterface
import com.example.datastorepoc.R
import com.example.datastorepoc.databinding.ActivityMigrateDatastoreBinding
import com.example.datastorepoc.migrationFromPrefDataStore.MigrationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MigrationToProtoDataStoreActivity : AppCompatActivity(), OnClickHandlerInterface {

    private lateinit var migrationManager: MigrationManagerProto
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMigrateDatastoreBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_migrate_datastore)
        binding.clickHandler = this

        setSharedPreferencesData("Kartik", "Garg")

    }

    private fun setSharedPreferencesData(firstName: String, lastName: String) {
        sharedPreferences = getSharedPreferences(
            MigrationManagerProto.USER_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString(MigrationManagerProto.USER_FIRST_NAME, firstName)
            .putString(MigrationManagerProto.USER_LAST_NAME, lastName)
            .apply()

        displaySharedPreferenceData()

    }

    private fun displaySharedPreferenceData() {
        binding.tvPrefFirstName.text = getString(
            R.string.pref_first_name,
            sharedPreferences.getString(MigrationManager.USER_FIRST_NAME, "")
        )
        binding.tvPrefLastName.text = getString(
            R.string.pref_last_name,
            sharedPreferences.getString(MigrationManager.USER_LAST_NAME, "")
        )

    }


    override fun onClick(view: View) {
        when (view) {
            binding.btnMigrate -> {
                migrateToPreferencesDataStore()
            }
        }
    }

    private fun migrateToPreferencesDataStore() {
        migrationManager = MigrationManagerProto(this)
        lifecycleScope.launch {
            migrationManager.readProto.collect {
                withContext(Dispatchers.Main) {

                    binding.tvDatastoreFirstName.text =
                        getString(R.string.dataStore_first_name, it.firstName)
                    binding.tvDatastoreLastName.text =
                        getString(R.string.dataStore_last_name, it.lastName)
                    displaySharedPreferenceData()
                }
            }
        }
    }


}