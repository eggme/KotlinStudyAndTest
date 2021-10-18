import type.Milk
import type.PayService

class KotlinStudyAndTestApp

fun main() {
    val milk = Milk(1, 1000, "ㅇㅇ")
    println(milk)
    PayService().pay(milk)
    println(milk)
}