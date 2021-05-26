package com.example.datastorepoc.migrationFromProtoDataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException


/**
 *To help with migration, DataStore defines the SharedPreferencesMigration class.
 * The by dataStore method that creates the DataStore, also exposes a produceMigrations parameter.
 * In this block we create the list of DataMigrations that should be run for this DataStore instance.
 * we have only one migration: the SharedPreferencesMigration
 *
 * SharedPreferencesView allows us to retrieve data from SharedPreferences
 */

private val Context.dataStore: DataStore<UserDetailsOuterClass.UserDetails> by dataStore(
    fileName = "user_details.pb",
    serializer = UserDetailsProtoSerializer,
    produceMigrations = {
        listOf(SharedPreferencesMigration(
            it,
            MigrationManagerProto.USER_PREFERENCES_NAME
        ) { sharedPrefs: SharedPreferencesView, currentData: UserDetailsOuterClass.UserDetails ->
            currentData.toBuilder()
                .setFirstName(sharedPrefs.getString(MigrationManagerProto.USER_FIRST_NAME, ""))
                .setLastName(sharedPrefs.getString(MigrationManagerProto.USER_LAST_NAME, ""))
                .build()
        })
    }
)

class MigrationManagerProto(context: Context) {


    val readProto: Flow<UserDetailsOuterClass.UserDetails> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(UserDetailsOuterClass.UserDetails.getDefaultInstance())
            } else {
                throw  ex
            }
        }


    companion object {
        const val USER_PREFERENCES_NAME = "my_preference"
        const val USER_FIRST_NAME = "my_firstname"
        const val USER_LAST_NAME = "my_lastname"

    }

}