package ar.edu.utn.frba.ddam.homie.entities

class Post {
    var id : Int
    var type : String
    var status : String
    var building : Building
    var price : Int
    var expenses : Int
    var currency : String
    var like : Boolean = false

    constructor(id: Int, type: String, status : String, building: Building, price: Int, expenses : Int, currency : String) {
        this.id = id
        this.type = type
        this.status = status
        this.building = building
        this.price = price
        this.expenses = expenses
        this.currency = currency
    }

    constructor(){
        this.id = 0
        this.type = ""
        this.status = ""
        this.building = Building()
        this.price = 0
        this.expenses = 0
        this.currency = ""
    }
}