package androidx.room
annotation class Dao
annotation class ForeignKey(
    val entity: kotlin.reflect.KClass<*>,
    val parentColumns: Array<String>,
    val childColumns: Array<String>,
    val onDelete: Int = 1,
) { companion object { const val CASCADE = 5 } }
annotation class Index(val value: Array<String>, val unique: Boolean = false)
annotation class Entity(
    val tableName: String = "",
    val foreignKeys: Array<ForeignKey> = [],
    val indices: Array<Index> = [],
)
annotation class PrimaryKey(val autoGenerate: Boolean = false)
annotation class Query(val value: String)
annotation class Insert(val onConflict: Int = 1)
annotation class Update
annotation class Delete
annotation class Database(
    val entities: Array<kotlin.reflect.KClass<*>> = [],
    val version: Int = 1,
    val exportSchema: Boolean = true,
)
object OnConflictStrategy { const val ABORT = 1; const val IGNORE = 2; const val REPLACE = 3 }
abstract class RoomDatabase
