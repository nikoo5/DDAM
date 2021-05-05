package ar.edu.utn.frba.ddam.homie.database

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.*

@Database(entities = [Post::class, Building::class, Location::class, User::class, UserPosts::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class LocalDatabase : RoomDatabase() {

    abstract fun postDao() : PostDao
    abstract fun buildingDao() : BuildingDao
    abstract fun locationDao() : LocationDao
    abstract fun userDao() : UserDao
    abstract fun userPostDao() : UserPostsDao

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getLocalDatabase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    //INSTANCE = Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "homieDB").allowMainThreadQueries().build()
                    INSTANCE = Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "homieDB").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyDatabase() {
            INSTANCE = null
        }

        fun initData(context: Context, user : User) {
            val db = getLocalDatabase(context)!!
            val userDao = db.userDao()
            val postDao = db.postDao()
            val buildingDao = db.buildingDao()
            val locationDao = db.locationDao()
            val userPostsDao = db.userPostDao()

            userPostsDao.clearTable();
            userPostsDao.resetTable();

            userDao.clearTable();
            userDao.resetTable();

            postDao.clearTable();
            postDao.resetTable();

            buildingDao.clearTable();
            buildingDao.resetTable();

            locationDao.clearTable();
            locationDao.resetTable()

            val userId = userDao.insert(user).toInt()

            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Teodoro Vilardebó", 2755, 6, "B", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Argerich", 3053, 3, "C", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Nogoya", 3000, 1, "A", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Simbron", 2900, 1, "A", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Remedios Escalada de San Martin", 3006, 6, "B", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Nazca", 1359, 4, "A", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Melincue", 3000, 3, "C", "lat", "long"))
            locationDao.insert(Location(0, "Capital Federal", "Villa del Parque", "Cuenca", 2000, 5, "B", "lat", "long"))

            buildingDao.insert(Building(0, 1, "apartment", (42).toLong(), (42).toLong(), 2));
            buildingDao.insert(Building(0, 2, "apartment", (27).toLong(), (33).toLong(), 1));
            buildingDao.insert(Building(0, 3, "apartment", (60).toLong(), (70).toLong(), 3));
            buildingDao.insert(Building(0, 4, "apartment", (81).toLong(), (90).toLong(), 4));
            buildingDao.insert(Building(0, 5, "house", (43).toLong(), (47).toLong(), 2));
            buildingDao.insert(Building(0, 6, "house", (31).toLong(), (31).toLong(), 1));
            buildingDao.insert(Building(0, 7, "apartment", (45).toLong(), (45).toLong(), 2));
            buildingDao.insert(Building(0, 8, "apartment", (32).toLong(), (32).toLong(), 2));

            postDao.insert(Post(0, 1, "rental", "available", 29000, 3200, "$"));
            postDao.insert(Post(0, 2, "rental", "reserved", 25000, 4300, "$"));
            postDao.insert(Post(0, 3, "sale", "available", 224600, 0, "U\$D"));
            postDao.insert(Post(0, 4, "sale", "reserved", 374212, 0, "U\$D"));
            postDao.insert(Post(0, 5, "rental", "available", 34000, 3100, "$"));
            postDao.insert(Post(0, 6, "rental", "available", 20000, 3000, "$"));
            postDao.insert(Post(0, 7, "rental", "available", 29500, 5200, "$"));
            postDao.insert(Post(0, 8, "rental", "available", 26000, 3500, "$"));

            val building = buildingDao.getById(1)!!
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159442.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159432.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159435.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159438.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159441.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159434.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159433.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159437.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159436.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159439.jpg")
            building.images.add("https://imgar.zonapropcdn.com/avisos/resize/1/00/44/41/01/94/1200x1200/1690159440.jpg")
            buildingDao.update(building)

            userPostsDao.insert(UserPosts(userId, 1))
            userPostsDao.insert(UserPosts(userId, 4))
        }
    }
}