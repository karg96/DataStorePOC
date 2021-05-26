package com.example.datastorepoc.migrationFromPrefDataStore

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastorepoc.pref.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by preferencesDataStore(name = MigrationManager.USER_PREFERENCES_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, MigrationManager.USER_PREFERENCES_NAME))
    }
)

class MigrationManager(context: Context) {


    val userDetailsFlow: Flow<UserDetails> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map {
            val firstName = it[PREF_FIRST_NAME] ?: "testFirstName"
            val lastName = it[PREF_LAST_NAME] ?: "testLastName"
            UserDetails(firstName, lastName)
        }

    companion object {
        const val USER_PREFERENCES_NAME = "my_preference"
        const val USER_FIRST_NAME = "my_firstname"
        const val USER_LAST_NAME = "my_lastname"

        val PREF_FIRST_NAME = stringPreferencesKey(USER_FIRST_NAME)
        val PREF_LAST_NAME = stringPreferencesKey(USER_LAST_NAME)
    }
}