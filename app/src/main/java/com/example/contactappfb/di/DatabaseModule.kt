package com.example.contactappfb.di

import android.content.Context
import com.example.contactappfb.data.ContactDao
import com.example.contactappfb.data.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContactDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getDatabase(context)
    }

    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.todoDao()
    }
}
