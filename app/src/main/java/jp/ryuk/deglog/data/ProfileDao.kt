package jp.ryuk.deglog.data

import androidx.room.*

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)

    @Query("DELETE FROM profile_table WHERE name = :name")
    fun deleteByName(name: String)

    @Query("SELECT * FROM profile_table ORDER BY name")
    fun getProfiles() : List<Profile>

    @Query("SELECT * FROM profile_table WHERE name = :key")
    fun getProfile(key: String) : Profile

    @Query("SELECT birthday FROM profile_table WHERE name = :name")
    fun getBirthday(name: String): Long?

    @Query("SELECT name FROM profile_table")
    fun getNames() : List<String>

    @Query("DELETE FROM profile_table")
    fun clear()

    @Query("SELECT name FROM profile_table WHERE name = :name")
    fun isRegistered(name: String): String?
}