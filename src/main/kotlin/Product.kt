package type

abstract class Product{
    open var pId: Int? = 0
    open var price: Int? = 0
    open var name: String? = null

    constructor(pId: Int?, price: Int?, name: String?){
        this.pId = pId
        this.price = price
        this.name = name
    }
}