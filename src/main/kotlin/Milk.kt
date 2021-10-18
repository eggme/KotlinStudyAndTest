package type

data class Milk(override var pId: Int?, override var price: Int?, override var name: String?): Product(pId, price, name), Discountable {

    override fun discount(): Int? {
        return price
    }
}

fun Discountable.discount(body: () -> Int): Int? {
    return body()
}
