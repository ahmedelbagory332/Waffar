package bego.market.waffar.ui.fragments.admin.order

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import bego.market.waffar.adapters.UserOrdersAdapter
import bego.market.waffar.models.UserOrder
import bego.market.waffar.utils.NetworkUtils
import com.greycellofp.tastytoast.TastyToast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.converter.ArabicConverter
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import dagger.hilt.android.AndroidEntryPoint
import bego.market.waffar.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class UserOrdersFragment : Fragment(),SearchView.OnQueryTextListener, PrintingCallback , MenuProvider {


    @Inject
    lateinit var userOrdersAdapter : UserOrdersAdapter
    private val orderViewModel: OrderViewModel by viewModels()
    private val deliveryViewModel: DeliveryViewModel by viewModels()
    private lateinit var allOrderLinearLayout: LinearLayout
    private var orderList: MutableList<UserOrder> = mutableListOf()
    lateinit var totalPrice: TextView
    private lateinit var allOrderPrice: TextView
    private lateinit var noteEditText: EditText
    private lateinit var noteText: String
    private lateinit var currentDate: String
    private var printing : Printing? = null
    private lateinit var printButton : Button
    private lateinit var unPairAndPair : Button
    private lateinit var user:UserOrdersFragmentArgs
    private val deliveryPrice = "10.00"
    private var totalPricePrinter:String=""
    private var allOrderPricePrinter: String=""
    private var receipt: String = ""
    private val costDelivery:Int = 10
    private var PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_user_order, container, false)

        val  rsSection: RecyclerView = view.findViewById(R.id.user_order)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar4)
        val buttonLoading: Button = view.findViewById(R.id.reLoading3)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val dialog = Dialog(activity!!)

        user = UserOrdersFragmentArgs.fromBundle(arguments!!)
        totalPrice = view.findViewById(R.id.admin_total_price)
        allOrderPrice = view.findViewById(R.id.admin_allOrderPrice)
        noteEditText = view.findViewById(R.id.editTextTextnote)
        allOrderLinearLayout = view.findViewById(R.id.admin_allOrder)
        printButton = view.findViewById(R.id.print_Button)
        unPairAndPair = view.findViewById(R.id.unPair_Pair_button)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<TextView>(R.id.dialogTitle).text = "جاري الحذف...."
        dialog.findViewById<TextView>(R.id.txtClose).setOnClickListener { dialog.dismiss() }
        currentDate = sdf.format(Date())

         val permReqLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    if (Printooth.hasPairedPrinter())
                    {
                        Printooth.removeCurrentPrinter()
                    }
                    else{
                        resultLauncher.launch(Intent(activity!!, ScanningActivity::class.java))
                        changePairAndUnpair()
                    }
                }else{
                    TastyToast.makeText(activity, "No Permission Granted", TastyToast.STYLE_ALERT).show()

                }
            }





        if (printing != null)
            printing?.printingCallback = this

        unPairAndPair.setOnClickListener {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (Printooth.hasPairedPrinter())
                {
                    Printooth.removeCurrentPrinter()
                }
                else{
                    resultLauncher.launch(Intent(activity!!, ScanningActivity::class.java))
                    changePairAndUnpair()
                }
            }

                if (hasPermissions(activity!!, PERMISSIONS)) {
                    if (Printooth.hasPairedPrinter())
                    {
                        Printooth.removeCurrentPrinter()
                    }
                    else{
                        resultLauncher.launch(Intent(activity!!, ScanningActivity::class.java))
                        changePairAndUnpair()
                    }
                } else {
                    permReqLauncher.launch(PERMISSIONS)
                }



        }

        printButton.setOnClickListener {
            if (hasPermissions(activity!!, PERMISSIONS)) {

                if (!Printooth.hasPairedPrinter())
                    resultLauncher.launch(Intent(activity!!, ScanningActivity::class.java))
                else
                    printText()
            }else {
                permReqLauncher.launch(PERMISSIONS)
            }

        }
        changePairAndUnpair()


        buttonLoading.setOnClickListener {
            orderViewModel.restVariables()
            orderViewModel.getUserOrders(user.mail)
        }
        orderViewModel.getUserOrders(user.mail)
        orderViewModel.responseForGetUserOrders.observe(viewLifecycleOwner) { orders ->
            orderList = orders
            userOrdersAdapter.submitList(orders)


            userOrdersAdapter.deleteButtonClick={ productPosition ->
                if (NetworkUtils().isInternetAvailable(activity!!)) {
                    dialog.show()
                    orderViewModel.deleteOrder(orders[productPosition].productNumber)
                    orders.removeAt(productPosition)
                    userOrdersAdapter.notifyDataSetChanged()
                    totalPrice.text = orders.sumOf { it.price.toInt() }.toString() + " جنية "
                    allOrderPrice.text = (orders.sumOf { it.price.toInt() } + costDelivery).toString() + " جنية "


                } else {
                    TastyToast.makeText(
                        activity,
                        "تاكد من اتصالك بالانترنت",
                        TastyToast.STYLE_ALERT
                    ).show()
                }
            }


            totalPrice.text = orders.sumOf { it.price.toInt() }.toString() + " جنية "
            allOrderPrice.text = (orders.sumOf { it.price.toInt() } + 10).toString() + " جنية "
            totalPricePrinter = orders.sumOf { it.price.toInt() }.toString()
            allOrderPricePrinter = (orders.sumOf { it.price.toInt() } + 10).toString()


            rsSection.adapter = userOrdersAdapter
            progressBar.visibility = View.GONE


            activity!!.viewModelStore.clear()

        }
        userOrdersAdapter.deliveryButtonClick={productUserEmail: String, productNumber: String ->

            if (NetworkUtils().isInternetAvailable(activity!!)) {
                deliveryViewModel.deliveryProduct(productUserEmail, productNumber)
            } else {
                TastyToast.makeText(
                    activity,
                    "تاكد من اتصالك بالانترنت",
                    TastyToast.STYLE_ALERT
                ).show()
            }
        }

        // respond from getUserOrders
        orderViewModel.connectionErrorForGetUserOrders.observe(viewLifecycleOwner) {
            when (it) {
                "حدث خطأ اثناء تحميل المنتجات" -> {
                    TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                    progressBar.visibility = View.GONE
                    buttonLoading.visibility = View.VISIBLE
                }
                "تم بنجاح" -> {
                    progressBar.visibility = View.GONE
                    buttonLoading.visibility = View.GONE
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    buttonLoading.visibility = View.GONE
                }
            }
        }

//        orderViewModel.emptyListForGetUserOrders.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty())
//                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
//            spinKitView.visibility = View.GONE
//        }

        // respond from deleteOrder
        orderViewModel.connectionErrorForDeleteOrder.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()
                progressBar.visibility = View.GONE
                dialog.dismiss()
            }
            orderViewModel.restVariables()
        }

        orderViewModel.responseForDeleteOrder.observe(viewLifecycleOwner) {
            if (it.status.isNotEmpty()) {
                TastyToast.makeText(activity, it.status, TastyToast.STYLE_CONFIRM).show()
                progressBar.visibility = View.GONE
                dialog.dismiss()
            }
            orderViewModel.restVariables()
        }

        // respond from deliveryProduct
        deliveryViewModel.connectionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                TastyToast.makeText(activity, it, TastyToast.STYLE_ALERT).show()

        }

        deliveryViewModel.response.observe(viewLifecycleOwner) {
            if (it.status.isNotEmpty())
                TastyToast.makeText(activity, it.status, TastyToast.STYLE_ALERT).show()

        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        val userInput = newText!!.lowercase(Locale.getDefault())
        val newList: MutableList<UserOrder> = mutableListOf()
        for (search in orderList) {
            if (search.productNumber.contains(userInput)) {
                newList.add(search)
            }
        }
        userOrdersAdapter.search(newList)
        return true
    }

    private fun printText() {
        noteText = if (noteEditText.text.isNotEmpty()) noteEditText.text.toString() else "نراكم قريبا"


        receipt = "${user.name}\n"+"${user.phone}\n "+"${user.address}\n "+"$currentDate\n"

        receipt += "-----------------------------------------------\n"
        receipt += String.format("%1$-10s %2$10s %3$13s ", "السعر", "الكميه", "المنتج")
        receipt += "\n"
        receipt += "-----------------------------------------------\n"
        for (i in 0 until orderList.size) {
            receipt += "\n"+String.format("%1$-10s %2$10s %3$13s ", orderList[i].name, orderList[i].productQuantity, orderList[i].price)
        }
        receipt += "\n-----------------------------------------------"+"\n "
        receipt += "\n"
        receipt += "-----------------------------------------------\n"
        receipt += String.format("%1$-10s %2$10s %3$13s ", "السعر", "التوصيل", "الاجمالى")
        receipt += "\n"
        receipt += "-----------------------------------------------\n"
        receipt +=  "\n "+String.format("%1$-10s %2$10s %3$13s ",totalPricePrinter, deliveryPrice, allOrderPricePrinter)
        receipt += "\n"
        receipt += "\n-----------------------------------------------"+"\n "
        receipt += "\n\n "
        receipt += noteText



          println(receipt)

        val printAbles = ArrayList<Printable>()
        val printable = TextPrintable.Builder()
            .setText(receipt) //The text you want to print
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setCharacterCode(DefaultPrinter.CHARCODE_ARABIC_CP864) // Character code to support languages
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setCustomConverter(ArabicConverter())
            .setNewLinesAfter(1)
            .build()
        printAbles.add(printable)
        Printooth.printer().print(printAbles)



    }

    private fun changePairAndUnpair() {
        unPairAndPair.text = if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"

    }

    override fun connectingWithPrinter() {
        Toast.makeText(activity!!,"Connecting to print", Toast.LENGTH_SHORT).show()
    }

    override fun connectionFailed(error: String) {
        Toast.makeText(activity!!, "failed :  $error", Toast.LENGTH_LONG).show()
    }

    override fun disconnected() {
        Toast.makeText(activity!!,"disconnected to print", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        Toast.makeText(activity!!,  error , Toast.LENGTH_LONG).show()
    }

    override fun onMessage(message: String) {
        Toast.makeText(activity!!, "message :  $message", Toast.LENGTH_LONG).show()
    }

    override fun printingOrderSentSuccessfully() {
        Toast.makeText(activity!!,"order sent to print" , Toast.LENGTH_LONG).show()
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            initPrinting()
        changePairAndUnpair()

    }

    private fun initPrinting() {
        if (!Printooth.hasPairedPrinter())
            printing = Printooth.printer()
        if (printing != null)
            printing?.printingCallback = this
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


}