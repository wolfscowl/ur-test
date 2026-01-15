package com.wolfscowl.ur_client.test


//class House() {
//    val place = "Gummersbach"
//}
//
// open class Person() {
//     open var name: String = ""
//     var age: Int = 2
//     var house: House = House()
// }
//
//
//data class PersonState(override var name: String): Person() {
//    var job: String = ""
//}
//
//fun main() {
//    val p1 = PersonState("Hans")
//    val p2 = p1.copy()
//
//    println("P1 = P2: " + (p1 == p2))
//
//    p1.house = House()
//    p1.age = 5
//    p1.job = "teo"
//
//    println(p1.job)
//    println(p2.job)
//
//    println("P1 = P2: " + (p1 == p2))
//
//}




//data class PersonState(var name: String, var job: String = "Trinken")  {
//
//}
//
//fun main() {
//    val p1 = PersonState("Hans")
//    val p2 = p1.copy()
//
//    println("P1 = P2: " + (p1 == p2))
//
//
//    p1.job = "essen"
//    //p1.name = "teo"
//
//    println(p1.job)
//    println(p2.job)
//
//    println("P1 = P2: " + (p1 == p2))
//
//}


data class PersonState(var name: String, var job: String = "Trinken")  {

}

fun main() {

    val list1 = mutableListOf("1","2","3")
    val list2 = list1.toMutableList()

    list2.add("d")

    println(list1 == list2)

}