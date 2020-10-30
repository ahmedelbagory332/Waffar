package bego.market.belbies.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import bego.market.belbies.Adapters.UserOrderAdapter
import bego.market.belbies.Models.OrdersUser
import bego.market.belbies.NetworkUtils

import bego.market.belbies.R
import bego.market.belbies.ViewModel.DeleteOrderViewModel
import bego.market.belbies.ViewModel.DeliveryViewModel
import bego.market.belbies.ViewModel.UserOrderViewModel
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.github.ybq.android.spinkit.SpinKitView
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.converter.ArabicConverter
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import com.sdsmdg.tastytoast.TastyToast
import java.text.SimpleDateFormat
import java.util.*

class UserOrderFragment : Fragment(),SearchView.OnQueryTextListener, PrintingCallback {

    var   userOrderAdapter : UserOrderAdapter? = null
    lateinit var userOrderViewModel: UserOrderViewModel
    lateinit var deleteOrderViewModel: DeleteOrderViewModel
    lateinit var deliveryViewModel: DeliveryViewModel
    lateinit var allOrderLinearLayout: LinearLayout

    var ordertList: MutableList<OrdersUser> = mutableListOf()
    lateinit var totalPrice: TextView
    lateinit var allOrderPrice: TextView
    lateinit var noteEditText: EditText
    lateinit var noteText: String


    lateinit var currentDate: String
    private var printing : Printing? = null
    lateinit var printButton : Button
     lateinit var unPair_Pair : Button
     var printerText = ""
    private lateinit var user:UserOrderFragmentArgs
    val deliveryPrice = "10.00"
     var totalPricePrinter:String=""
     var allOrderPricePrinter: String=""
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_user_order, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.user_order)
        val spinKitView = view.findViewById<SpinKitView>(R.id.spin_kit)
         user = UserOrderFragmentArgs.fromBundle(arguments!!)
        totalPrice = view.findViewById(R.id.admin_total_price)
        allOrderPrice = view.findViewById(R.id.admin_allOrderPrice)
        noteEditText = view.findViewById(R.id.editTextTextnote)
        allOrderLinearLayout = view.findViewById(R.id.admin_allOrder)
        printButton = view.findViewById(R.id.print_Button)
        unPair_Pair = view.findViewById(R.id.unPair_Pair_button)



        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
         currentDate = sdf.format(Date())





        if (printing != null)
            printing?.printingCallback = this

        unPair_Pair.setOnClickListener {
            if (Printooth.hasPairedPrinter())
            {
                Printooth.removeCurrentPrinter()
            }
            else{
                startActivityForResult(
                    Intent(activity!!, ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
                ChangeParirAndUnpair()
            }
        }
        printButton.setOnClickListener {
            if (!Printooth.hasPairedPrinter())
                startActivityForResult(
                    Intent(activity!!, ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
            else
                printText()


        }
        ChangeParirAndUnpair()

        val dialog = ACProgressFlower.Builder(activity!!)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("جارى التحميل")
            .fadeColor(Color.DKGRAY).build()

        deleteOrderViewModel = ViewModelProvider(activity!!).get(DeleteOrderViewModel::class.java)
        deliveryViewModel = ViewModelProvider(activity!!).get(DeliveryViewModel::class.java)
        userOrderViewModel = ViewModelProvider(activity!!).get(UserOrderViewModel::class.java)


        userOrderViewModel.setEmail(user.mail)


        userOrderViewModel.getUserOrders()
        userOrderViewModel.userOrders.observe(viewLifecycleOwner, Observer { list ->
            ordertList = list.ordersUser



            userOrderAdapter = UserOrderAdapter(
                UserOrderAdapter.DeleteOnClickListener {
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        dialog.show()
                         deleteOrderViewModel.deleteOrders(list.ordersUser[it].productNumber)
                        list.ordersUser.removeAt(it)
                        userOrderAdapter?.notifyDataSetChanged()
                        totalPrice.text=list.ordersUser.sumBy { it.price.toInt() }.toString()+" جنية "
                        allOrderPrice.text= (list.ordersUser.sumBy { it.price.toInt() } + 10).toString() +" جنية "


                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                UserOrderAdapter.DeliveryOnClickListener { productUserEmail: String, productNumber: String->
                    if (NetworkUtils().isInternetAvailable(activity!!)){
                        dialog.show()
                         deliveryViewModel.uploadProduct(productUserEmail,productNumber)
                    }
                    else{
                        TastyToast.makeText(context,  "تاكد من اتصالك بالانترنت", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                    }
                },
                context,
                list.ordersUser)
            totalPrice.text=list.ordersUser.sumBy { it.price.toInt() }.toString()+" جنية "
            allOrderPrice.text= (list.ordersUser.sumBy { it.price.toInt() } + 10).toString() +" جنية "
            totalPricePrinter = list.ordersUser.sumBy { it.price.toInt() }.toString()
            allOrderPricePrinter = (list.ordersUser.sumBy { it.price.toInt() } + 10).toString()


                       rsSection.adapter = userOrderAdapter
                       spinKitView.visibility = View.GONE
            allOrderLinearLayout.visibility = View.VISIBLE


            activity!!.viewModelStore.clear()

        })

        userOrderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()


        })
        deliveryViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        deliveryViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,  it.status, TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
            activity!!.viewModelStore.clear()
            dialog.dismiss()

        })

        userOrderViewModel.emptyList.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.CONFUSING).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()

        })

        deleteOrderViewModel.connectionError.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
            dialog.dismiss()


        })
        deleteOrderViewModel.response.observe(viewLifecycleOwner, Observer {
            TastyToast.makeText(context,it.status,TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
            spinKitView.visibility = View.GONE
            activity!!.viewModelStore.clear()
            dialog.dismiss()


        })

        setHasOptionsMenu(true)

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {



        val userinput = newText!!.toLowerCase()
        val newlist: MutableList<OrdersUser> = mutableListOf()
        for (search in ordertList) {
            if (search.productNumber.contains(userinput)) {
                newlist.add(search)
            }
        }
        userOrderAdapter?.search(newlist)
        return true
    }

    private fun printText() {
        noteText = if (!noteEditText.text.isEmpty()) noteEditText.text.toString() else "نراكم قريبا"



        /*
        * version1 -> CHARCODE_ARABIC_CP864
        * version2 -> CHARCODE_ARABIC_CP864 with converter
        * version3 -> CHARCODE_ARABIC_CP720
        * version4 -> CHARCODE_ARABIC_CP720 with converter
        * */

        var BILL = ""

        BILL = "${user.name}\n"+"${user.phone}\n "+"${user.address}\n "+"$currentDate\n"

        BILL += "-----------------------------------------------\n";
        BILL += String.format("%1$-10s %2$10s %3$13s ", "السعر", "الكميه", "المنتج");
        BILL += "\n";
        BILL += "-----------------------------------------------\n";
        for (i in 0 until ordertList.size) {
            BILL += "\n"+String.format("%1$-10s %2$10s %3$13s ", ordertList[i].name, ordertList[i].productQuantity, ordertList[i].price)
        }
        BILL += "\n-----------------------------------------------"+"\n "
        BILL += "\n";
        BILL += "-----------------------------------------------\n";
        BILL += String.format("%1$-10s %2$10s %3$13s ", "السعر", "التوصيل", "الاجمالى")
        BILL += "\n";
        BILL += "-----------------------------------------------\n";
        BILL +=  "\n "+String.format("%1$-10s %2$10s %3$13s ",totalPricePrinter, deliveryPrice, allOrderPricePrinter)
        BILL += "\n";
        BILL += "\n-----------------------------------------------"+"\n "
        BILL += "\n\n ";
        BILL += noteText



          println(BILL)

        val printables = ArrayList<Printable>()
        val printable = TextPrintable.Builder()
            .setText(BILL) //The text you want to print
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setCharacterCode(DefaultPrinter.CHARCODE_ARABIC_CP864) // Character code to support languages
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setCustomConverter(ArabicConverter())
            .setNewLinesAfter(1)
            .build()
        printables.add(printable)
        Printooth.printer().print(printables)



    }

    private fun ChangeParirAndUnpair() {
        unPair_Pair.text = if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"

    }

    override fun connectingWithPrinter() {
        Toast.makeText(activity!!,"Connecting to print", Toast.LENGTH_SHORT).show()
    }

    override fun connectionFailed(error: String) {
        Toast.makeText(activity!!,"failed :  "+ error , Toast.LENGTH_LONG).show()
    }

    override fun onError(error: String) {
        Toast.makeText(activity!!,  error , Toast.LENGTH_LONG).show()
    }

    override fun onMessage(message: String) {
        Toast.makeText(activity!!,"message :  "+ message , Toast.LENGTH_LONG).show()
    }

    override fun printingOrderSentSuccessfully() {
        Toast.makeText(activity!!,"order sent to print" , Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            initPrinting()
        ChangeParirAndUnpair()
    }

    private fun initPrinting() {
        if (!Printooth.hasPairedPrinter())
            printing = Printooth.printer()
        if (printing != null)
            printing?.printingCallback = this
    }

}