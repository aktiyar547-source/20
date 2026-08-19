package androidx.room
object Room {
    fun <T : RoomDatabase> databaseBuilder(context: Any?, klass: Class<T>, name: String): Builder<T> =
        throw RuntimeException()
    class Builder<T> {
        fun addMigrations(vararg m: androidx.room.migration.Migration): Builder<T> = this
        fun fallbackToDestructiveMigration(): Builder<T> = this
        fun build(): T = throw RuntimeException()
    }
}
