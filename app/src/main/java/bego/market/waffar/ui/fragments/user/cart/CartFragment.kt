package bego.market.waffar.ui.fragments.user.cart

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.R
import bego.market.waffar.adapters.CartAdapter
import bego.market.waffar.ui.fragments.admin.order.OrderViewModel
import bego.market.waffar.utils.UserValidation
import com.greycellofp.tastytoast.TastyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class CartFragment : Fragment(){

    @Inject
    lateinit var   cartAdapter : CartAdapter
    private val cartViewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    @Inject
    lateinit var userValidation: UserValidation
    private lateinit var noProductLinearLinearLayout: LinearLayout
    private lateinit var allOrderLinearLayout: LinearLayout
    lateinit var totalPrice:TextView
    private lateinit var allOrderPrice:TextView
    private val costDelivery:Int = 10
   // private var dialogOrderNumber:KAlertDialog? = null
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_cart, container, false)

        val  cartRecyclerView: RecyclerView = view.findViewById(R.id.cartItem)
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري الطلب...."
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }



        noProductLinearLinearLayout = view.findViewById(R.id._noProductLinear)
        allOrderLinearLayout = view.findViewById(R.id._allOrder)
        totalPrice = view.findViewById(R.id._total_price)
        allOrderPrice = view.findViewById(R.id._allOrderPrice)



        cartViewModel.getProductsFromCart()
        cartViewModel.allProduct.observe(viewLifecycleOwner) { productList ->

            if (productList.isEmpty()) {
                noProductLinearLinearLayout.visibility = View.VISIBLE
                allOrderLinearLayout.visibility = View.GONE

            }
            else {
                noProductLinearLinearLayout.visibility = View.GONE
                allOrderLinearLayout.visibility = View.VISIBLE

                totalPrice.text = productList.sumOf { it.totalPrice.toInt() }.toString() + " جنية "
                allOrderPrice.text = (productList.sumOf { it.totalPrice.toInt() } + costDelivery).toString() + " جنية "
                cartAdapter.submitList(productList)
                cartRecyclerView.adapter = cartAdapter
            }
            cartAdapter.deleteButtonClick={ productId: Long, productPosition: Int ->
                cartViewModel.removeProductInCart(productId)
                productList.removeAt(productPosition)
                cartAdapter.notifyDataSetChanged()
                if (productList.isEmpty()) {
                    noProductLinearLinearLayout.visibility = View.VISIBLE
                    allOrderLinearLayout.visibility = View.GONE

                }
            }

            cartAdapter.orderClick={order , productPosition ->
                if (userValidation.readLoginStatus()) {
                    if (userValidation.readEmail()
                            .isEmpty() || userValidation.readAddress()
                            .isEmpty() || userValidation.readPhone().isEmpty()
                    ) {
                        TastyToast.makeText(
                            activity,
                            "برجاء ادخال عنوان",
                            TastyToast.STYLE_MESSAGE
                        ).show()

                    } else {
                        dialog.show()
                        lifecycleScope.launch {
                            val requestOrder = async {
                                orderViewModel.requestOrder(
                                    order.productName,
                                    order.productImage,
                                    order.totalPrice,
                                    userValidation.readEmail(),
                                    userValidation.readAddress(),
                                    order.productQuantity,
                                    userValidation.readPhone(),
                                    userValidation.readName()
                                )
                            }
                            withContext(Dispatchers.Main) {
                                if (requestOrder.await()){
                                    cartViewModel.removeProductInCart(productList[productPosition].id)
                                    productList.removeAt(productPosition)
                                    cartAdapter.notifyDataSetChanged()
                                    if (productList.isEmpty()) {
                                        noProductLinearLinearLayout.visibility = View.VISIBLE
                                        allOrderLinearLayout.visibility = View.GONE

                                    }
                                }
                            }


                        }

                    }

                } else {
                    TastyToast.makeText(
                        activity,
                        "برجاء تسجيل الدخول",
                        TastyToast.STYLE_MESSAGE
                    ).show()
                }
            }


        }






        cartViewModel.productId.observe(viewLifecycleOwner) {
            if(it == 1)
              TastyToast.makeText(activity, "تم الحذف من العربة", TastyToast.STYLE_MESSAGE).show()
            cartViewModel.setProductId(0)
        }

        orderViewModel.responseForRequestOrder.observe(viewLifecycleOwner) {
             if(it.isNotEmpty()) {
                 dialog.dismiss()
                 showProductNumberDialog(it)
             }
        }

        orderViewModel.connectionErrorForRequestOrder.observe(viewLifecycleOwner) {
            if(it == "حدث خطأ اثناء التحميل") {
                dialog.dismiss()
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
            }
        }


        return view
    }



    private fun showProductNumberDialog(productNumber:String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "تم طلب المنتج برجاء حفظ رقم الطلب"
        dialog.findViewById<TextView>(R.id.txtProductNumber).text = "رقم الطلب : $productNumber"
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

}