package bego.market.belbies.Fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bego.market.belbies.R
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.SpinKitView
import androidx.lifecycle.Observer
import bego.market.belbies.ViewModel.*
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import com.sdsmdg.tastytoast.TastyToast


class DetailsFragment : Fragment() {

    lateinit var detailsViewModel: DetailsViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var   cart : Cart
    var quantity:Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_details, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = CartDatabase.getInstance(application).cartDatabaseDao
        val viewModelFactory = CartViewModelFactory(dataSource, application)
        val position = DetailsFragmentArgs.fromBundle(arguments!!)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
        val productDetailsConstraintLayout = view.findViewById<ConstraintLayout>(R.id.productDetailsconstraintLayout)
        val productImage: ImageView = view.findViewById(R.id.product_details_img)
        val productName: TextView = view.findViewById(R.id.product_details_name)
        val productDescription: TextView = view.findViewById(R.id.product_details_des)
        val addProductToCart: Button = view.findViewById(R.id.details_cart_order)
        val productQuantityEditText : TextView = view.findViewById(R.id.editTextProductQuantity)

        val PlusQuantity: Button = view.findViewById(R.id.plusQuantity)
        val MinsQuantity: Button = view.findViewById(R.id.minsQuantity)

        cart  = Cart()

        detailsViewModel = ViewModelProvider(activity!!).get(DetailsViewModel::class.java)
        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)

        detailsViewModel.setProductId(position.id)




        detailsViewModel.getProductDetails()
        detailsViewModel.detailsResponse.observe(viewLifecycleOwner, Observer {

         productName.text = it.name
        productDescription.text = it.description
        Glide.with(context!!).load(it.image).into(productImage)

        cart.productId = it.id
        cart.productName = it.name
        cart.productImage = it.image
        if (it.productOfferPrice.isEmpty()) cart.totalPrice = it.price else cart.totalPrice = it.productOfferPrice

        spinKitView.visibility = View.GONE
        productDetailsConstraintLayout.visibility = View.VISIBLE

            activity!!.viewModelStore.clear()
        })

        detailsViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })

        addProductToCart.setOnClickListener {
            if (productQuantityEditText.text.isEmpty()){
                TastyToast.makeText(context,"لم يتم تحديد الكميه", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }
            else {


                cart.productQuantity = productQuantityEditText.text.toString()
                cart.totalPrice = "${productQuantityEditText.text.toString().toInt()*cart.totalPrice.toString().toInt()}"
                cartViewModel.setProductInCart(cart)
            }
        }

        PlusQuantity.setOnClickListener {
            quantity++
            productQuantityEditText.setText("${quantity}")
        }

        MinsQuantity.setOnClickListener {
                 if (quantity!=1){
                     quantity--
                     productQuantityEditText.setText("${quantity}")
                 }

        }

        cartViewModel.cart.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,  "تم الاضافة الى العربة", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            activity!!.viewModelStore.clear()

        })




        return view
    }


}