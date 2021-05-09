package ar.edu.utn.frba.ddam.homie.database

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.*
import kotlin.math.abs

@Database(entities = [Post::class, Building::class, Location::class, User::class, UserPosts::class], version = 1, exportSchema = false)
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

        private fun initLocations(context: Context) {
            val db = getLocalDatabase(context)!!
            val locationDao = db.locationDao()

            locationDao.clearTable();
            locationDao.resetTable();

            locationDao.insert(Location(0, "eDJtoRCdhWwK", "Capital Federal", "Villa del Parque", "Teodoro Vilardebó", 2755, 6, "B", -34.60495596510264, -58.49648048643036))
            locationDao.insert(Location(0, "8NY5LV2xJy09", "Capital Federal", "Villa del Parque", "Argerich", 3053, 3, "C", -34.60150434465808, -58.49091441526641))
            locationDao.insert(Location(0, "EngpUmy0zG9s", "Capital Federal", "Villa del Parque", "Nogoya", 3000, 1, "A", -34.60218581462346, -58.49052195944592))
            locationDao.insert(Location(0, "aMe0nEjQMZ0d", "Capital Federal", "Villa del Parque", "Simbron", 2900, 1, "A", -34.597351143790526, -58.492932745953695))
            locationDao.insert(Location(0, "0ICpG2let88E", "Capital Federal", "Villa del Parque", "Remedios Escalada de San Martin", 3006, 6, "B", -34.61613692994664, -58.47964243060962))
            locationDao.insert(Location(0, "2IZ7Srk6OyGp", "Capital Federal", "Villa del Parque", "Nazca", 1359, 4, "A", -34.61744112126074, -58.476254128758335))
            locationDao.insert(Location(0, "8tcgdIhGA55o", "Capital Federal", "Villa del Parque", "Melincue", 3000, 3, "C", -34.60136898218604, -58.491735972938336))
            locationDao.insert(Location(0, "32tlyAjeoTlo", "Capital Federal", "Villa del Parque", "Cuenca", 2000, 5, "B", -34.61145158953019, -58.486897630609754))
        }

        private fun initBuildings(context: Context) {
            val db = getLocalDatabase(context)!!
            val buildingDao = db.buildingDao()

            buildingDao.clearTable();
            buildingDao.resetTable();

            buildingDao.insert(Building(0, "fuJe3fS2ILhH", 1, "apartment", (42).toLong(), (42).toLong(), 2, 1, 1, 50));
            buildingDao.insert(Building(0, "RH1z3DcZ0Wbj", 2, "apartment", (27).toLong(), (33).toLong(), 1, 1, 1, 5));
            buildingDao.insert(Building(0, "lyDf9OM6uq6V", 3, "apartment", (60).toLong(), (70).toLong(), 3, 1, 2, 1));
            buildingDao.insert(Building(0, "6XuZDFxFnymT", 4, "apartment", (81).toLong(), (90).toLong(), 4, 2, 2, 50));
            buildingDao.insert(Building(0, "hvFSkEtfP3Jf", 5, "house", (43).toLong(), (47).toLong(), 2, 1, 1, 0));
            buildingDao.insert(Building(0, "QL4rnD9H6hlW", 6, "house", (31).toLong(), (31).toLong(), 1, 1, 1, 0));
            buildingDao.insert(Building(0, "bF7Zadh6004t", 7, "apartment", (45).toLong(), (45).toLong(), 2, 1, 1, 40));
            buildingDao.insert(Building(0, "fMvVSYEBiUyB", 8, "apartment", (32).toLong(), (32).toLong(), 2, 1, 1, 0));

            var building : Building = buildingDao.getById(1)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F0.jpg?alt=media&token=c58d3b80-3fcd-4c09-b279-7281c147179e")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F1.jpg?alt=media&token=3ab0f420-a698-4bb8-99aa-94dfacda0d8b")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F2.jpg?alt=media&token=2faf9bc0-18a7-47d4-b083-9f75f96c7bf8")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F3.jpg?alt=media&token=8099f100-00f7-43ea-91a4-b9bc0fea42ab")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F4.jpg?alt=media&token=c79e3a35-4a7c-4f60-a17c-d4cf46148542")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F5.jpg?alt=media&token=0436cd2f-5679-46ce-9e38-a90001ab0f5c")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F6.jpg?alt=media&token=307d1782-7f22-40aa-a49c-c2ce252dcc44")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F7.jpg?alt=media&token=abc4aff7-09d9-4b87-94fb-e191f53e17f9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F8.jpg?alt=media&token=7d706b2a-6f10-4005-8c56-bd90f394720a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F9.jpg?alt=media&token=979227b9-6343-4074-aba6-01f880f05fec")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfuJe3fS2ILhH%2F10.jpg?alt=media&token=2d4fd67e-7397-49f3-b7ca-658901ae4c68")
            buildingDao.update(building)

            building = buildingDao.getById(2)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F11.jpg?alt=media&token=fae24fcb-90de-49b1-9523-2fdf0b3386a2")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F10.jpg?alt=media&token=6ee40244-86ed-4700-8c09-87afa5df3dc9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F12.jpg?alt=media&token=c2195306-9524-48fd-9f83-7bd59093dd3a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F13.jpg?alt=media&token=cc1cb309-1bfa-4580-af8f-05d2044faec7")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F14.jpg?alt=media&token=9b63e65f-0dbc-4721-844e-cc36ce009862")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F15.jpg?alt=media&token=5d0923a5-e88e-496e-8e24-3f39a7350b3c")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F16.jpg?alt=media&token=fabdf90a-5125-4891-83cc-95c90701bc0b")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F17.jpg?alt=media&token=69f66e85-5238-44a0-9608-71c95c8e95c5")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F18.jpg?alt=media&token=c81f54f2-08cc-43eb-8172-6cec11e6b26b")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F19.jpg?alt=media&token=6fc721f9-f1c5-4220-a457-a136460e30c3")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FRH1z3DcZ0Wbj%2F20.jpg?alt=media&token=dd46c31f-440e-43ff-862f-85c4a1b91854")
            buildingDao.update(building)

            building = buildingDao.getById(3)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F60.jpg?alt=media&token=fcaa1d7d-5dd4-4a6a-814c-a895013abbe9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F45.jpg?alt=media&token=4b2064eb-65f2-4cae-b048-e8143991a901")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F46.jpg?alt=media&token=03d2aee8-6d6e-41db-a168-82c2811cac0a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F47.jpg?alt=media&token=d5bbbcb6-ac55-4c91-aa0e-75f6e2ed6805")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F48.jpg?alt=media&token=a3cd0ede-1270-4776-a432-9eddaa648178")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F49.jpg?alt=media&token=ec97defd-c733-4b48-b59e-435901e795b3")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F50.jpg?alt=media&token=9349c648-2465-48bd-a503-c5a6f5202826")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F51.jpg?alt=media&token=4f3b35af-be71-4c2b-92f0-ceec6d0793d7")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F52.jpg?alt=media&token=03e5e632-9ad6-4a17-b3b3-ce356030eeec")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F53.jpg?alt=media&token=cb26eccd-41a9-43fa-abee-3112f46a8afe")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F54.jpg?alt=media&token=6cc3bf50-582f-40bd-a24a-4e3b7560c4bf")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F55.jpg?alt=media&token=e271f0f7-64a5-4cb4-ac60-c57647709c86")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F56.jpg?alt=media&token=cb2e340d-0420-4a53-bb12-27abd89f8444")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F57.jpg?alt=media&token=1604a6b1-169e-44b7-8e9e-c5ffcd685457")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F58.jpg?alt=media&token=6f288d63-560b-4934-a691-60e63d8627f1")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F59.jpg?alt=media&token=3bee9f5a-8a5a-4cbb-96b2-14366dbe201f")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FlyDf9OM6uq6V%2F60.jpg?alt=media&token=fcaa1d7d-5dd4-4a6a-814c-a895013abbe9")
            buildingDao.update(building)

            building = buildingDao.getById(4)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F91.jpg?alt=media&token=94cccdf5-7e4a-4cc9-9489-29dc5f626358")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F85.jpg?alt=media&token=3e861cc6-cf28-4c37-8acc-309386d9cb41")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F86.jpg?alt=media&token=82118b52-516a-42cc-8aa6-2977e406781e")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F87.jpg?alt=media&token=8aeaff30-04fb-4c91-81cc-5ac6da5792fa")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F88.jpg?alt=media&token=0498173d-5366-41ee-ac80-15be76e4f05a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F89.jpg?alt=media&token=d93d0a44-d906-4078-adb7-6152be061f96")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F90.jpg?alt=media&token=508bccc9-df63-4720-93bd-24b95b0d1dfd")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F92.jpg?alt=media&token=4ee869b2-f6c4-4ac8-a014-443f22395c8d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F93.jpg?alt=media&token=6cec3f95-8460-4598-bfb9-b17724486550")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F94.jpg?alt=media&token=ff5f6dbe-9a2a-4de8-9835-58cd2d6db646")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F95.jpg?alt=media&token=64b37916-e516-4cc7-be3e-f2e78384f45a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F96.jpg?alt=media&token=b5a7b9e5-66eb-448b-aea6-2b46cad0f8f2")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F97.jpg?alt=media&token=27fc7e54-b4c7-4fb5-a85f-6f6aa3cd88bc")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F98.jpg?alt=media&token=d053657d-3b05-46a9-9a04-265788d5ecac")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2F6XuZDFxFnymT%2F99.jpg?alt=media&token=d8c6db4a-4bd0-4dee-b401-0e73a1994f84")
            buildingDao.update(building)

            building = buildingDao.getById(5)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F79.jpg?alt=media&token=4ec5e977-9312-4610-b464-946bb514d680")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F18.jpg?alt=media&token=22f17c19-7f4c-4b6f-b739-60fbeb9a4552")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F19.jpg?alt=media&token=ee871a49-e67c-495f-9329-82c4f0389906")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F20.jpg?alt=media&token=8b51eb6c-855c-43af-a553-d4931eb45a1b")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F21.jpg?alt=media&token=eefb0049-cf93-44f9-adc7-f381679ba5b1")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F73.jpg?alt=media&token=42adf195-5d94-4f2a-9b04-8b77168e60a8")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F74.jpg?alt=media&token=b522b2c3-2f26-4d6d-b7da-c35c7bf9b046")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F75.jpg?alt=media&token=c779e965-5fb4-430e-86fb-f559a7a1b722")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F76.jpg?alt=media&token=aaca2827-2863-4cf2-9166-cc2a7962d180")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F77.jpg?alt=media&token=b70f1c2b-bfa6-48f5-a096-4e00d66034d5")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F78.jpg?alt=media&token=edd2be14-49bd-46ee-b362-d8af6b82705d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F80.jpg?alt=media&token=67997b71-2467-4fe4-8ca4-80f470eb77bd")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F81.jpg?alt=media&token=b6737bca-569e-449f-a764-228e69a355c4")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F82.jpg?alt=media&token=3eb1d73a-11e1-48b7-ad97-201294fede61")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F83.jpg?alt=media&token=d1b60b5c-d63d-4075-84e1-4b787eafdd3a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F84.jpg?alt=media&token=26ff117c-9955-4e75-af96-ca99fef04f3a")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FhvFSkEtfP3Jf%2F85.jpg?alt=media&token=29d9d4f5-9aeb-44eb-be4d-c508c7ef27d8")
            buildingDao.update(building)

            building = buildingDao.getById(6)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FQL4rnD9H6hlW%2F77.jpg?alt=media&token=a683bce6-bf0f-415b-a6a3-8b19fb50c865")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FQL4rnD9H6hlW%2F63.jpg?alt=media&token=d9ed33ff-6599-4248-8a88-2b066bb18cb5")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FQL4rnD9H6hlW%2F64.jpg?alt=media&token=6ca3d122-034f-4a9c-b2e5-67e26543017b")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FQL4rnD9H6hlW%2F65.jpg?alt=media&token=4db87ed5-b3ff-4de6-a952-1b256be4d2c5")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FQL4rnD9H6hlW%2F76.jpg?alt=media&token=1b6daf4b-b8ae-46a2-8ada-7b883cb7cf7a")
            buildingDao.update(building)

            building = buildingDao.getById(7)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F37.jpg?alt=media&token=08d2a0bb-b546-41f8-97a6-414ed5c2dfe0")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F36.jpg?alt=media&token=a7144339-8b34-4312-adb1-29acb114c00d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F38.jpg?alt=media&token=f01344c0-8c2b-46f6-a403-f258eed6e88f")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F39.jpg?alt=media&token=6c7e74e5-adb6-4e2d-abdc-4f6c7ea1d5aa")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F40.jpg?alt=media&token=0a856eb9-881a-4d23-9243-ec2a579dc2f9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F41.jpg?alt=media&token=b283e370-992b-48cc-904f-b76cd83a8346")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F42.jpg?alt=media&token=5dbef1a9-a497-448c-b337-1aee9e9ae93e")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F43.jpg?alt=media&token=a0f8903a-870d-4ecc-86f0-c5b0a953fa31")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F44.jpg?alt=media&token=774cbcdd-bf4c-4b41-b8d0-c7c1546f8cb9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F45.jpg?alt=media&token=d680621a-3ec7-4fdc-adea-95c158124a06")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F46.jpg?alt=media&token=27cabf0f-d7c4-43a1-ae93-fe4ef07df5ec")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F47.jpg?alt=media&token=06297ebd-02ff-4f7b-8fee-71884ebaedb4")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F48.jpg?alt=media&token=d9310d4b-9f71-4737-a91a-4f18af46f591")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F49.jpg?alt=media&token=53760a24-0477-4c41-9727-73a5f229933d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F50.jpg?alt=media&token=fd62065d-933c-40de-8791-c8de70333d76")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F51.jpg?alt=media&token=968677bf-0d2c-47b1-a4c0-05760c2ae9c7")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FbF7Zadh6004t%2F52.jpg?alt=media&token=a031bff3-507e-4061-9d88-3a5ed74fdeba")
            buildingDao.update(building)

            building = buildingDao.getById(8)!!
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F85.jpg?alt=media&token=600a255d-aace-4d36-92a3-400485db359d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F78.jpg?alt=media&token=32923ecb-de1d-4af7-9bb1-135ef002c8fa")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F79.jpg?alt=media&token=53597bff-234c-44ee-896f-289e0a12f5b9")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F80.jpg?alt=media&token=02343c91-6eeb-450e-8bcd-efcb47a8c824")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F81.jpg?alt=media&token=5a6fcb08-53de-4060-b585-503f40664063")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F82.jpg?alt=media&token=74caa200-6981-4c41-8beb-6762a39a9759")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F83.jpg?alt=media&token=70ef62eb-4ece-49ff-a155-9740bc6f485d")
            building.images.add("https://firebasestorage.googleapis.com/v0/b/homie-b5662.appspot.com/o/building_images%2FfMvVSYEBiUyB%2F84.jpg?alt=media&token=285c444b-6041-4ac8-9f2a-7b323857d540")
            buildingDao.update(building)
        }

        private fun initPosts(context: Context) {
            val db = getLocalDatabase(context)!!
            val postDao = db.postDao()

            postDao.clearTable();
            postDao.resetTable();

            postDao.insert(Post(0, "KDldkEIucDru", 1, "rental", "available", 29000, 3200, "$", "+5491140913030"));
            postDao.insert(Post(0, "822qswrzWqFx", 2, "rental", "reserved", 25000, 4300, "$", "+5491140913030"));
            postDao.insert(Post(0, "8ZPdisnkhkHo", 3, "sale", "available", 224600, 0, "U\$D", "+5491140913030"));
            postDao.insert(Post(0, "pOX3uqNVQdaD", 4, "sale", "reserved", 374212, 0, "U\$D", "+5491140913030"));
            postDao.insert(Post(0, "DKwD5FqZ7RyR", 5, "rental", "available", 34000, 3100, "$", "+5491140913030"));
            postDao.insert(Post(0, "7wdQ2F48peI3", 6, "rental", "available", 20000, 3000, "$", "+5491140913030"));
            postDao.insert(Post(0, "EcuCcbOi7t7c", 7, "rental", "available", 29500, 5200, "$", "+5491140913030"));
            postDao.insert(Post(0, "mNLbA7jDc8Py", 8, "rental", "available", 26000, 3500, "$", "+5491140913030"));
        }

        fun initData(context: Context) {
            initLocations(context);
            initBuildings(context);
            initPosts(context);
        }

        fun updateLocationFromCloud(context: Context, data : Location.LocationCloud) : Int {
            val db = getLocalDatabase(context)!!
            var location = db.locationDao().getByDbId(data.id)
            if(location != null) {
                if(location.lastUpdate < data.last_update) {
                    location.city = data.city
                    location.district = data.district
                    location.address = data.address
                    location.number = data.number
                    location.floor = data.floor
                    location.apartment = data.apartment
                    location.latitude = data.latitude
                    location.longitude = data.longitude
                    location.lastUpdate = data.last_update
                    db.locationDao().update(location)
                    return  location.id
                }
            } else {
                location = Location(0, data.id, data.city, data.district, data.address, data.number, data.floor, data.apartment, data.latitude, data.longitude)
                location!!.lastUpdate = data.last_update
                return db.locationDao().insert(location).toInt()
            }
            return 0 - location.id;
        }

        fun updateBuildingFromCloud(context: Context, data : Building.BuildingCloud) : Int {
            val db = getLocalDatabase(context)!!
            val locationId = updateLocationFromCloud(context, data.location)
            var building = db.buildingDao().getByDbId(data.id)
            if(building != null) {
                if(building.lastUpdate < data.last_update || locationId > 0) {
                    building.locationId = abs(locationId)
                    building.type = data.type
                    building.surface = data.surface
                    building.surfaceOpen = data.surface_open
                    building.rooms = data.rooms
                    building.bathrooms = data.bathrooms
                    building.bedrooms = data.bedrooms
                    building.antique = data.antique
                    building.features = data.features
                    building.images = data.images
                    building.lastUpdate = data.last_update
                    db.buildingDao().update(building)
                    return building.id
                }
            } else {
                building = Building(0, data.id, abs(locationId), data.type, data.surface, data.surface_open, data.rooms, data.bathrooms, data.bedrooms, data.antique)
                building!!.features = data.features
                building!!.images = data.images
                building!!.lastUpdate = data.last_update
                return db.buildingDao().insert(building).toInt()
            }
            return 0 - building.id
        }

        fun updatePostFromCloud(context: Context, data : Post.PostCloud) : Int {
            val db = getLocalDatabase(context)!!
            val buildingId = updateBuildingFromCloud(context, data.building)
            var post = db.postDao().getByDbId(data.id)
            if(post != null) {
                if(post.lastUpdate < data.last_update || buildingId > 0) {
                    post.buildingId = abs(buildingId)
                    post.type = data.type
                    post.status = data.status
                    post.price = data.price
                    post.expenses = data.expenses
                    post.currency = data.currency
                    post.contactPhone = data.contact_phone
                    post.viewCount = data.view_count
                    post.lastUpdate = data.last_update
                    db.postDao().update(post)
                    return post.id
                }
            } else {
                post = Post(0, data.id, abs(buildingId), data.type, data.status, data.price, data.expenses, data.currency, data.contact_phone)
                post!!.viewCount = data.view_count
                post!!.lastUpdate = data.last_update
                return  db.postDao().insert(post).toInt()
            }
            return 0 - post.id
        }
    }
}