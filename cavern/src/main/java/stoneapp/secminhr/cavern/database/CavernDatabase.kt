package stoneapp.secminhr.cavern.database

import android.content.Context
import androidx.room.*
import java.net.HttpCookie
import java.net.URI

@Database(entities = [CookieEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CavernDatabase: RoomDatabase() {
    abstract fun cookieDao(): CookieDao

    companion object {
        private var instance: CavernDatabase? = null
        fun getInstance(context: Context): CavernDatabase {
            return instance ?: Room.databaseBuilder(context.applicationContext,
                    CavernDatabase::class.java,
                    "cavern-database").build().also { instance = it }
        }
    }
}

@Dao
abstract class CookieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: CookieEntity)

    @Delete
    abstract fun delete(vararg entities: CookieEntity)

    @Query("SELECT * from cookies")
    abstract fun getAll(): List<CookieEntity>?

    @Query("SELECT * from cookies WHERE uri LIKE :uri AND `key` LIKE :key AND value LIKE :value AND version LIKE :version LIMIT 1")
    abstract fun findBy(uri: String, key: String, value: String, version: Int): CookieEntity?

    fun findBy(uri: URI?, cookie: HttpCookie): CookieEntity? {
        return findBy(uri.toString(), cookie.name, cookie.value, cookie.version)
    }
}

@Entity(tableName = "cookies")
data class CookieEntity(
        val uri: URI?,
        val key: String,
        val value: String,
        val version: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

class Converters {
    @TypeConverter
    fun fromString(str: String): URI? {
        return if(str == "null") {
            null
        } else {
            URI.create(str)
        }
    }
    @TypeConverter
    fun fromURI(uri: URI?):String {
        return uri?.toString() ?: "null"
    }
}