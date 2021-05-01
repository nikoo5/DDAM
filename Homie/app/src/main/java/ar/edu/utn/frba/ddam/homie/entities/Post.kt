package ar.edu.utn.frba.ddam.homie.entities

class Post {
    var uid : String
    var type : String
    var status : String
    var building : Building
    var price : Int
    var expenses : Int
    var currency : String

    constructor(uid: String, type: String, status : String, building: Building, price: Int, expenses : Int, currency : String) {
        this.uid = uid
        this.type = type
        this.status = status
        this.building = building
        this.price = price
        this.expenses = expenses
        this.currency = currency
    }

    constructor(){
        this.uid = ""
        this.type = ""
        this.status = ""
        this.building = Building()
        this.price = 0
        this.expenses = 0
        this.currency = ""
    }
}