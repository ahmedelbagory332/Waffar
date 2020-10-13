package bego.market.belbies.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.CartAdapter
import bego.market.belbies.R
import bego.market.belbies.UserValidation
import bego.market.belbies.ViewModel.CartDatabase
import bego.market.belbies.ViewModel.CartViewModel
import bego.market.belbies.ViewModel.OrderViewModel
import bego.market.belbies.ViewModel.ViewModelFactory.CartViewModelFactory
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
 import com.developer.kalert.KAlertDialog
import com.sdsmdg.tastytoast.TastyToast


class CartFragment : Fragment(){

    lateinit var   cartAdapter : CartAdapter
    lateinit var cartViewModel: CartViewModel
    lateinit var orderViewModel: OrderViewModel

    lateinit var userValidation: UserValidation
    lateinit var reLoadingLinearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_cart, container, false)

        val  rsCart: RecyclerView = view.findViewById(R.id.cartItem)

        val application = requireNotNull(this.activity).application
        val dataSource = CartDatabase.getInstance(application).cartDatabaseDao
        val viewModelFactory = CartViewModelFactory(dataSource, application)
        userValidation = UserValidation(activity!!)

        reLoadingLinearLayout = view.findViewById(R.id._reloadingLinear)


        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        cartViewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        cartViewModel.cartProducts.observe(viewLifecycleOwner, Observer { list ->

            if (list.isEmpty()){
                reLoadingLinearLayout.visibility = View.VISIBLE
            }
            else
            {
                reLoadingLinearLayout.visibility = View.GONE
                cartAdapter =
                    CartAdapter(
                        CartAdapter.DeleteOnClickListener {
                            cartViewModel.removeProductInCart(list[it].productId)
                            list.removeAt(it)
                            cartAdapter.notifyDataSetChanged()
                        },

                        CartAdapter.OrderOnClickListener {
                            if (userValidation.readLoginStatus()){
                                if (userValidation.readEmail().isEmpty()||userValidation.readAddress().isEmpty()){
                                    TastyToast.makeText(context,  "برجاء ادخال عنوان", TastyToast.LENGTH_LONG, TastyToast.INFO).show()

                                }
                                else{
                                    dialog.show()
                                    orderViewModel.addOrder(it.productName,it.productImage,it.totalPrice,userValidation.readEmail(),userValidation.readAddress())
                                }

                            }
                            else{
                                TastyToast.makeText(context,  "برجاء تسجيل الدخول", TastyToast.LENGTH_LONG, TastyToast.INFO).show()
                            }

                        },
                        application)
                cartAdapter.submitList(list)
                rsCart.adapter = cartAdapter
//            val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                    return false
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
//                    //Remove swiped item from list and notify the RecyclerView
//                    cartViewModel.removeProductInCart(list[viewHolder.layoutPosition].productId)
//                    list.removeAt(viewHolder.layoutPosition)
//                    cartAdapter.notifyDataSetChanged()
//                }
//            }
//            ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rsCart)
                activity!!.viewModelStore.clear()

            }


        })

        cartViewModel.productId.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,  "تم الحذف من العربة", TastyToast.LENGTH_LONG, TastyToast.INFO).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        orderViewModel.serverResponse.observe(viewLifecycleOwner, Observer {
            showProductNumber(it)
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        orderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })




        return view
    }

    fun showProductNumber(productNumber:String){
        KAlertDialog(activity!!, KAlertDialog.WARNING_TYPE)
            .setTitleText("برجاء حفظ رقم الطلب")
            .setContentText("رقم الطلب : "+productNumber)
            .show();
    }


}