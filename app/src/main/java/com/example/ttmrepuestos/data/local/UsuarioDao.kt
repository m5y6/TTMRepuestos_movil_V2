package com.example.ttmrepuestos.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<Usuario>>

    @Query("SELECT * FROM users WHERE correo = :email")
    suspend fun getUserByEmail(email: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usuario: Usuario): Long

    @Update
    suspend fun updateUser(usuario: Usuario)

    @Delete
    suspend fun deleteUser(usuario: Usuario)
}
