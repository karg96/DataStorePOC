package com.example.datastorepoc.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException


/**
 *  Context.createDataStore() extension function to create an instance of DataStore<UserSettings>.
 *  The filename parameter tells DataStore which file to use to store the data, and the serializer
 *  parameter tells DataStore the name of the serializer class
 */
class ProtoSettingsManager(val context: Context) {
    val Context.datatStore: DataStore<UserInfo.UserSettings> by dataStore(
        fileName = "user_settings.pb",
        serializer = ProtoSettingsSerializer
    )

    val userSettingsFlow: Flow<UserInfo.UserSettings> = context.datatStore.data.catch { ex ->
        if (ex is IOException) {
            emit(UserInfo.UserSettings.getDefaultInstance())
        } else {
            throw ex
        }
    }

    /**
     * Proto DataStore provides an updateData() function that transactionally updates a stored object.
     * updateData() gives you the current state of the data as an instance of your data type and updates
     * the data transactionally in an atomic read-write-modify operation
     */

    suspend fun updateColor(bgColor: UserInfo.UserSettings.BgColor) {
        context.datatStore.updateData {
            it.toBuilder().setBgColor(bgColor).build()
        }
    }

}