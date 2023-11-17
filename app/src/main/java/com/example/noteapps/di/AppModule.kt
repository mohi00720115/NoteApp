package com.example.noteapps.di

import android.app.Application
import androidx.room.Room
import com.example.noteapps.data.local.dao.NoteDao
import com.example.noteapps.data.local.db.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //---------------------------------------- Data Base -----------------------------------------//

    @Provides
    @Singleton
    fun provideDatabase(application: Application): NotesDatabase {
        return Room.databaseBuilder(application, NotesDatabase::class.java, "note_database").build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDataBase: NotesDatabase): NoteDao {
        return noteDataBase.noteDao()
    }

}