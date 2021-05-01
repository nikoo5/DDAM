package ar.edu.utn.frba.ddam.homie.entities

class Location {
    var city : String
    var district : String
    var address : String
    var number : Int
    var floor : Int
    var appartment : String
    var latitude : String
    var longitude : String

    constructor(city : String, district : String, address: String, number: Int, floor: Int, appartment: String, latitude : String, longitude : String) {
        this.city = city
        this.district = district
        this.address = address
        this.number = number
        this.floor = floor
        this.appartment = appartment
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor() {
        this.city = ""
        this.district = ""
        this.address = ""
        this.number = 0
        this.floor = 0
        this.appartment = ""
        this.latitude = ""
        this.longitude = ""
    }
}