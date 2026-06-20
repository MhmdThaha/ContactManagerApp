package com.example.contactappfb.di

import com.example.contactappfb.repository.ContactRepository
import com.example.contactappfb.repository.OfflineContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindContactRepository(
        impl: OfflineContactsRepository
    ): ContactRepository
}
