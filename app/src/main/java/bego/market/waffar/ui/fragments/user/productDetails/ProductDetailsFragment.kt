package bego.market.waffar.ui.fragments.user.productDetails

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bego.market.waffar.R
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.SpinKitView
import bego.market.waffar.data.localDataBase.Cart
import bego.market.waffar.ui.fragments.user.cart.CartViewModel
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    @Inject
    lateinit var   cart : Cart
    private var quantity:Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_details, container, false)


        val product = ProductDetailsFragmentArgs.fromBundle(arguments!!)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
        val productDetailsConstraintLayout = view.findViewById<ConstraintLayout>(R.id.productDetailsconstraintLayout)
        val productImage: ImageView = view.findViewById(R.id.product_details_img)
        val productName: TextView = view.findViewById(R.id.product_details_name)
        val productDescription: TextView = view.findViewById(R.id.product_details_des)
        val addProductToCart: Button = view.findViewById(R.id.details_cart_order)
        val productQuantityText : TextView = view.findViewById(R.id.textProductQuantity)
        val productPrice : TextView = view.findViewById(R.id.tvPrice)
        val productPriceOffer : TextView = view.findViewById(R.id.tvPriceOffer)
        val plusQuantity: Button = view.findViewById(R.id.plusQuantity)
        val minusQuantity: Button = view.findViewById(R.id.minsQuantity)



        productDetailsViewModel.getProductDetails(product.id)
        productDetailsViewModel.detailsResponse.observe(viewLifecycleOwner) { productDetails ->

            productName.text = productDetails.name
            productDescription.text = productDetails.description
            productPrice.text = productDetails.price
            if(productDetails.productOfferPrice == "0")
                productPriceOffer.text = "لا يوجد خصم علي هذا المنتج"
            else
              productPriceOffer.text = productDetails.productOfferPrice

            Glide.with(context!!).load(productDetails.image).into(productImage)

            cart.productId = productDetails.id
            cart.productName = productDetails.name
            cart.productImage = productDetails.image

        }

        productDetailsViewModel.connectionError.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء التحميل" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    spinKitView.visibility = View.GONE
                }
                "تم بنجاح" -> {
                    spinKitView.visibility = View.GONE
                    productDetailsConstraintLayout.visibility = View.VISIBLE
                }
                else -> {
                    spinKitView.visibility = View.VISIBLE
                }
            }

        }

        addProductToCart.setOnClickListener {
            if(productPriceOffer.text == "لا يوجد خصم علي هذا المنتج")
                cart.totalPrice = "${productQuantityText.text.toString().toInt()*productPrice.text.toString().toInt()}"
             else
                cart.totalPrice = "${productQuantityText.text.toString().toInt()*productPriceOffer.text.toString().toInt()}"

            cart.productQuantity = productQuantityText.text.toString()
            cartViewModel.setProductInCart(cart)
        }

        plusQuantity.setOnClickListener {
            quantity++
            productQuantityText.text = "$quantity"
        }

        minusQuantity.setOnClickListener {
                 if (quantity!=1){
                     quantity--
                     productQuantityText.text = "$quantity"
                 }
        }



        cartViewModel.cart.observe(viewLifecycleOwner) {
            if(it == 1)
             TastyToast.makeText(activity, "تم الاضافة الى العربة", TastyToast.STYLE_MESSAGE).show()
        }




        return view
    }


}