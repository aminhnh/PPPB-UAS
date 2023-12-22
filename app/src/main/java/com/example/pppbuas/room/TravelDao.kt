package com.example.pppbuas.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TravelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(travel: TravelEntity)
    @Update
    fun update(travel: TravelEntity)
    @Delete
    fun delete(travel : TravelEntity)
    @Query("DELETE FROM travel_table")
    fun deleteAll()
    @Query("DELETE FROM travel_table WHERE id = :travelId")
    fun deleteById(travelId: Int)

    @get:Query("SELECT * FROM travel_table ORDER BY id ASC")
    val allTravels: LiveData<List<TravelEntity>>
}