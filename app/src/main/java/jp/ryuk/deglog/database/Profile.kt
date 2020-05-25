package jp.ryuk.deglog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.ryuk.deglog.utilities.*
import java.time.LocalDate
import java.time.Period
import java.util.*

@Entity(tableName = PROFILE_TABLE)
data class Profile (
    @PrimaryKey var name: String,
    var type: String? = null,
    var gender: String? = null,
    var birthday: Long? = null,
    @ColumnInfo(name = "weight_unit") var weightUnit: String = "g",
    @ColumnInfo(name = "length_unit") var lengthUnit: String = "mm",
    var color: Int? = null
)