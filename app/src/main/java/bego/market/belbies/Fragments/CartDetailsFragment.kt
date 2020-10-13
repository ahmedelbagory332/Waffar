package bego.market.belbies.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import bego.market.belbies.R
import com.bumptech.glide.Glide


class CartDetailsFragment : Fragment() {



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_cart_details, container, false)


        val product = CartDetailsFragmentArgs.fromBundle(arguments!!)

        val cartDetailsProductImage: ImageView = view.findViewById(R.id.tv_cart_imageView)
        val cartDetailsProductName: TextView = view.findViewById(R.id.tv_cart_name)
        val cartDetailsProductCount: TextView = view.findViewById(R.id.tv_cart_count)
        val cartDetailsProductPrice: TextView = view.findViewById(R.id.tv_cart_price)




        cartDetailsProductName.text = "اسم المنتج : "+product.name
        cartDetailsProductCount.text = "الكمية : "+product.count
        cartDetailsProductPrice.text = "اجمالى السعر : "+product.price+" جنية "
        Glide.with(context!!).load(product.img).into(cartDetailsProductImage)







        return view
    }


}