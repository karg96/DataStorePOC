package com.example.datastorepoc.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastorepoc.UserPreferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PrefSettingsManager(val context:Context) {
 val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREF)

    /**
    Because Preferences DataStore does not use a predefined schema,
    you must use the corresponding key type function to define a key
    for each value that you need to store in the DataStore<Preferences> instance
     */

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { preferences->
            val color = preferences[BG_COLOR]?: android.R.color.white
            UserPreferences(color)
        }


    /**
     * Preferences DataStore provides an edit() function that
     * transactionally updates the data in a DataStore.
     * The function's transform parameter accepts a block of code
     * where you can update the values as needed. All of the code in the transform block is
     * treated as a single transaction.
     */

    suspend fun updateColor(color:Int){
        context.dataStore.edit { preferences->
            preferences[BG_COLOR] = color

        }
    }



    companion object{
        val BG_COLOR= intPreferencesKey("background_color")
        const val SETTINGS_PREF="settings_pref"
    }
}