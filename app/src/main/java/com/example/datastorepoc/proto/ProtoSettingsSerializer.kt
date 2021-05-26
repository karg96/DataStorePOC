package com.example.datastorepoc.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

/**
 * create a class which implements Serializer<T>, where T is the type defined in the proto file.
 * In our app, UserSettings is the type. This serializer class tells DataStore
 * how to read and write your data type
 */
object ProtoSettingsSerializer : Serializer<UserInfo.UserSettings> {


    override suspend fun readFrom(input: InputStream): UserInfo.UserSettings {
        try {
            return UserInfo.UserSettings.parseFrom(input)
        }catch (ex: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read protos", ex)
        }
    }

    override suspend fun writeTo(t: UserInfo.UserSettings, output: OutputStream) {
        t.writeTo(output)
    }

    override val defaultValue: UserInfo.UserSettings
        = UserInfo.UserSettings.getDefaultInstance()

}
