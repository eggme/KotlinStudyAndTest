package type

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.locks.Lock
import kotlin.text.StringBuilder

class PayService {

    fun pay(product: Product) {
        if(product is Discountable){
            product.discount()
            product?.apply {
                this.price = product.discount { product.price!! - 500 }
            }
        }
        println(product)
        twoAndThree {a, b -> a + b}
        println("adb123dc".filter_ { it in 'a'..'z' })

        val list = listOf("Alpha", "Beta")
        println(list.joiningToString())
        println(list.joiningToString(transform = {it.toUpperCase()}))
        println(list.joiningToString(separator = "!", prefix = "[", postfix = "]") )

        val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
        println("Shipping costs ${calculator(Order(3))}")

        val contacts = listOf(Person("Dmitry", "Jemerov", "123-456"),
            Person("Svetlana", "Isakova", null))

        val contactListFilters = ContactListFilters()
        with(contactListFilters) {
            prefix = "Dm"
            onlyWithPhoneNumber = true
        }
        println(contacts.filter(contactListFilters.getPredicate()))

        val log = listOf(
            SiteVisit("/", 34.0, OS.WINDOWS),
            SiteVisit("/", 22.0, OS.MAC),
            SiteVisit("/login", 12.0, OS.LINUX),
            SiteVisit("/signup", 8.0, OS.IOS),
            SiteVisit("/", 16.3, OS.ANDROID),
            SiteVisit("/", 34.6, OS.WINDOWS))
        println(SiteVisit::duration)
        val averageWindowDuration = log.filter { it.os == OS.WINDOWS }.map(SiteVisit::duration).average()
        println(averageWindowDuration)
        println(log.averageDurationFor { it.os in setOf(OS.MAC, OS.ANDROID) } )
    }



    inline fun <T> synchronizedTest(lock: Lock, action: () -> T): T {
        lock.lock()
        try{
            return action()
        }finally {
            lock.unlock()
        }
    }

    fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) = filter(predicate).map(SiteVisit::duration).average()

    data class SiteVisit(
        val path: String,
        val duration: Double,
        val os: OS
    )

    enum class OS{ WINDOWS, LINUX, MAC, IOS, ANDROID }

    data class Person(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String?
    )

    class ContactListFilters {
        var prefix: String = ""
        var onlyWithPhoneNumber: Boolean = false

        fun getPredicate(): (Person) -> Boolean {
            val startsWithPrefix =  { p : Person ->
                p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
            }
            if(!onlyWithPhoneNumber) {
                return startsWithPrefix
            }
            return {startsWithPrefix(it) && it.phoneNumber != null}
        }
    }


    fun twoAndThree(operation: (Int, Int) -> Int) {
        val result = operation(2, 3)
        println(result)
    }

    enum class Delivery { STANDARD, EXPEDITED }

    class Order(val itemCount: Int)

    fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {
        if(delivery == Delivery.EXPEDITED) {
            return { order -> 6 + 2.1 * order.itemCount }
        }
        return {order -> 2.1 * order.itemCount}
    }

    fun <T> Collection<T>.joiningToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String  = "",
        transform: (T) -> String = {it.toString()}
        ): String {
        val result = StringBuilder(prefix)
        for((index, element) in this.withIndex()) {
            if(index > 0) result.append(separator)
            result.append(transform?.invoke(element) ?: element.toString())
        }
        result.append(postfix)
        return result.toString()
    }

    fun String.filter_(a: (Char) -> Boolean): String {
        println(toString())
        val sb = StringBuilder()
        for(index in 0 until length){
            val element = get(index)
            if(a(element)) sb.append(element)
        }
        return sb.toString()
    }
}