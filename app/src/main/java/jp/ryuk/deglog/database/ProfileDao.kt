package jp.ryuk.deglog.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile_table WHERE name = :name")
    fun getProfile(name: String) : LiveData<Profile?>

    @Query("SELECT * FROM profile_table ORDER BY name")
    fun getAllProfile() : LiveData<List<Profile>>

    @Query("SELECT name FROM profile_table")
    fun getNames() : LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)

    @Query("DELETE FROM profile_table WHERE name = :name")
    suspend fun deleteByName(name: String)
}