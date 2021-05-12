package ar.edu.utn.frba.ddam.homie.entities

class FunctionsResponse {
    var errorCode : Int = -99
    var errorMessage : String = ""
    var result : Int

    constructor(map : Map<String, Any>) {
        errorCode = map["errorCode"] as Int
        errorMessage = map["errorMessage"] as String
        result = map["result"] as Int

    }
}