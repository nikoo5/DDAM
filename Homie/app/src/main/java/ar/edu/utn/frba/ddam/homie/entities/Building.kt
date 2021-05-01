package ar.edu.utn.frba.ddam.homie.entities

class Building {
    var type : String
    var location : Location
    var surface : Long
    var surface_open : Long
    var rooms : Int
    var features : MutableList<String>
    var images : MutableList<String> = mutableListOf()

    constructor(type: String, location: Location, surface: Long, surface_open: Long, rooms: Int, features: MutableList<String> = mutableListOf()) {
        this.type = type
        this.location = location
        this.surface = surface
        this.surface_open = surface_open
        this.rooms = rooms
        this.features = features
    }

    constructor() {
        this.type = ""
        this.location = Location()
        this.surface = 0
        this.surface_open = 0
        this.rooms = 0
        this.features = mutableListOf()
    }
}
