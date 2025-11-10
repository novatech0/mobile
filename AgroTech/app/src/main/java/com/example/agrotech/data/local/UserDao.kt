package com.example.agrotech.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserEntity)
    @Delete
    suspend fun delete(entity: UserEntity)
    @Update
    suspend fun update(entity: UserEntity)
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>
}