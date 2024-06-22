package com.wenovate.spejz.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wenovate.spejz.data.datasource.GroceryDataSource
import com.wenovate.spejz.data.datasource.UserDataSource
import com.wenovate.spejz.data.repository.AuthRepository
import com.wenovate.spejz.data.repository.GroceryRepository
import com.wenovate.spejz.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideGroceryDataSource(firestore: FirebaseFirestore): GroceryDataSource {
        return GroceryDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(firestore: FirebaseFirestore): UserDataSource {
        return UserDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideGroceryRepository(
        groceryDataSource: GroceryDataSource
    ): GroceryRepository {
        return GroceryRepository(groceryDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository {
        return UserRepository(userDataSource)
    }
}