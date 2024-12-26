package com.meezzi.localtalk.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.meezzi.localtalk.repository.AuthRepository
import com.meezzi.localtalk.repository.BoardRepository
import com.meezzi.localtalk.repository.ChatRepository
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.PostSaveRepository
import com.meezzi.localtalk.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        credentialManager: CredentialManager,
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepository(credentialManager, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): HomeRepository {
        return HomeRepository(context, fusedLocationProviderClient)
    }

    @Singleton
    @Provides
    fun provideBoardRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): BoardRepository {
        return BoardRepository(firestore, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): ChatRepository {
        return ChatRepository(firestore, firebaseAuth)
    }

    @Singleton
    @Provides
    fun providePostSaveRepository(
        firestore: FirebaseFirestore,
        fireStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth
    ): PostSaveRepository {
        return PostSaveRepository(firestore, fireStorage, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        fireStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth
    ): UserRepository {
        return UserRepository(firestore, fireStorage, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage
}