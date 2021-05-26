package com.example.datastorepoc.migrationFromProtoDataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserDetailsProtoSerializer : Serializer<UserDetailsOuterClass.UserDetails> {
    override val defaultValue: UserDetailsOuterClass.UserDetails = UserDetailsOuterClass.UserDetails.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDetailsOuterClass.UserDetails {
        try {
            return UserDetailsOuterClass.UserDetails.parseFrom(input)
        }catch (exception: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read protos", exception)
        }
    }

    override suspend fun writeTo(t: UserDetailsOuterClass.UserDetails, output: OutputStream) {
        t.writeTo(output)
    }

}