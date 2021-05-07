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

            locationDao.insert(Location(0, "eDJtoRCdhWwK", "Capital Federal", "Villa del Parque", "Teodoro Vilardebó", 2755, 6, "B", -34.60495596510264, -58.49648048643036))
            locationDao.insert(Location(0, "8NY5LV2xJy09", "Capital Federal", "Villa del Parque", "Argerich", 3053, 3, "C", -34.60150434465808, -58.49091441526641))
            locationDao.insert(Location(0, "EngpUmy0zG9s", "Capital Federal", "Villa del Parque", "Nogoya", 3000, 1, "A", -34.60218581462346, -58.49052195944592))
            locationDao.insert(Location(0, "aMe0nEjQMZ0d", "Capital Federal", "Villa del Parque", "Simbron", 2900, 1, "A", -34.597351143790526, -58.492932745953695))
            locationDao.insert(Location(0, "0ICpG2let88E", "Capital Federal", "Villa del Parque", "Remedios Escalada de San Martin", 3006, 6, "B", -34.61613692994664, -58.47964243060962))
            locationDao.insert(Location(0, "2IZ7Srk6OyGp", "Capital Federal", "Villa del Parque", "Nazca", 1359, 4, "A", -34.61744112126074, -58.476254128758335))
            locationDao.insert(Location(0, "8tcgdIhGA55o", "Capital Federal", "Villa del Parque", "Melincue", 3000, 3, "C", -34.60136898218604, -58.491735972938336))
            locationDao.insert(Location(0, "32tlyAjeoTlo", "Capital Federal", "Villa del Parque", "Cuenca", 2000, 5, "B", -34.61145158953019, -58.486897630609754))

            buildingDao.insert(Building(0, "fuJe3fS2ILhH", 1, "apartment", (42).toLong(), (42).toLong(), 2, 1, 1, 50));
            buildingDao.insert(Building(0, "RH1z3DcZ0Wbj", 2, "apartment", (27).toLong(), (33).toLong(), 1, 1, 1, 5));
            buildingDao.insert(Building(0, "lyDf9OM6uq6V", 3, "apartment", (60).toLong(), (70).toLong(), 3, 1, 2, 1));
            buildingDao.insert(Building(0, "6XuZDFxFnymT", 4, "apartment", (81).toLong(), (90).toLong(), 4, 2, 2, 50));
            buildingDao.insert(Building(0, "hvFSkEtfP3Jf", 5, "house", (43).toLong(), (47).toLong(), 2, 1, 1, 0));
            buildingDao.insert(Building(0, "QL4rnD9H6hlW", 6, "house", (31).toLong(), (31).toLong(), 1, 1, 1, 0));
            buildingDao.insert(Building(0, "bF7Zadh6004t", 7, "apartment", (45).toLong(), (45).toLong(), 2, 1, 1, 40));
            buildingDao.insert(Building(0, "fMvVSYEBiUyB", 8, "apartment", (32).toLong(), (32).toLong(), 2, 1, 1, 0));

            postDao.insert(Post(0, "KDldkEIucDru", 1, "rental", "available", 29000, 3200, "$", "+5491140913030"));
            postDao.insert(Post(0, "822qswrzWqFx", 2, "rental", "reserved", 25000, 4300, "$", "+5491140913030"));
            postDao.insert(Post(0, "8ZPdisnkhkHo", 3, "sale", "available", 224600, 0, "U\$D", "+5491140913030"));
            postDao.insert(Post(0, "pOX3uqNVQdaD", 4, "sale", "reserved", 374212, 0, "U\$D", "+5491140913030"));
            postDao.insert(Post(0, "DKwD5FqZ7RyR", 5, "rental", "available", 34000, 3100, "$", "+5491140913030"));
            postDao.insert(Post(0, "7wdQ2F48peI3", 6, "rental", "available", 20000, 3000, "$", "+5491140913030"));
            postDao.insert(Post(0, "EcuCcbOi7t7c", 7, "rental", "available", 29500, 5200, "$", "+5491140913030"));
            postDao.insert(Post(0, "mNLbA7jDc8Py", 8, "rental", "available", 26000, 3500, "$", "+5491140913030"));

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
        }
    }
}