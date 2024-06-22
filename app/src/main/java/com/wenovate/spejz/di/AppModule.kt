package com.wenovate.spejz.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wenovate.spejz.data.datasource.GroceryDataSource
import com.wenovate.spejz.data.repository.AuthRepository
import com.wenovate.spejz.data.repository.GroceryRepository
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepository(auth)

    @Provides
    @Singleton
    fun provideGroceryDataSource(firestore: FirebaseFirestore): GroceryDataSource = GroceryDataSource(firestore)

    @Provides
    @Singleton
    fun provideGroceryRepository(groceryDataSource: GroceryDataSource): GroceryRepository = GroceryRepository(groceryDataSource)

}