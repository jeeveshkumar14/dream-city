package com.example.splash_screen

import android.Manifest
import android.R.attr.bitmap
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddIssuesActivity : AppCompatActivity() {

    private val PERMISSION_IMAGE_CAPTURE = 100
    lateinit var BT_agree: Button
    var count:Int = 0

    var encodedByte: ByteArray? = null
    var get_image: String = "No"
    var encoded: String = ""
    var new_file_name: String = ""
    lateinit var uri : Uri
    lateinit var Image_bitmap: Bitmap
    lateinit var uploaded_image : ImageView
    lateinit var fetch_location : ImageView
    lateinit var fetched_locations: TextView
    lateinit var tv_date_time: TextView
    lateinit var btn_back:AppCompatImageButton
    lateinit var add_notes: EditText

    var latitude: String? = ""
    var longitude: String? = ""
    var pincode: String? = ""
    var address_max: String? = ""
    var address_line0: String? = ""
    var address1: String? = ""
    var address2: String? = ""
    var area: String? = ""
    var state: String? = ""
    var street: String? = ""
    var currentDateandTime: String = ""
    var email : String="";
    var value : String="";

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_issues)

        BT_agree = findViewById(R.id.BT_agree)
        btn_back = findViewById(R.id.btn_back)
        uploaded_image = findViewById(R.id.uploaded_image)
        fetch_location = findViewById(R.id.fetch_location)
        fetched_locations = findViewById(R.id.fetched_locations)
        add_notes = findViewById(R.id.add_notes)
        tv_date_time = findViewById(R.id.tv_date_time)
        BT_agree.text = "Add compliant Image"
        var bundle : Bundle;
        value=Intent().getBundleExtra("User").toString();
        email=Intent().getBundleExtra("Email").toString();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.requestLocationUpdates(
                LocationRequest.create(),
                mLocationCallback,
                null
            )
        } else {
            Toast.makeText(
                this,
                "Location permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }


        BT_agree.setOnClickListener(View.OnClickListener {
            if (BT_agree.text.toString() == "Add compliant Image") {

                Dexter.withContext(this@AddIssuesActivity)
                    .withPermissions(
                        Manifest.permission.CAMERA
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                openCamera()
                            } else {
                                Toast.makeText(
                                  this@AddIssuesActivity,
                                    "Please Enable the Camera permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            token!!.continuePermissionRequest()
                        }

                    }).check()

            }
            else {
                if (validateFields()) {
//                    if (Main_prefence.getInstance().issuesdata.size != 0) {
//                        for (j in Main_prefence.getInstance().issuesdata.indices) {
//
//                            Log.e("Test","size = " +
//                                    Main_prefence.getInstance().issuesdata[j].landmark)
//
//                            if (!Main_prefence.getInstance().issuesdata[j].landmark
//                                    .equals(add_notes.text.toString())) {
//
//                                val addedissues: AddedIssuesDataModel = AddedIssuesDataModel()
//                                addedissues.login_type = Main_prefence.getInstance().user_login_type
//                                addedissues.username = Main_prefence.getInstance().user_first_name
//                                addedissues.Image = Image_bitmap
//                                addedissues.fetched_location = fetched_locations.text.toString()
//                                addedissues.landmark = add_notes.text.toString()
//                                addedissues.date_time = currentDateandTime
//                                Main_prefence.getInstance().issuesdata.add(addedissues)
//                                onBackPressed()
//
//                            }else {
//                                Toast.makeText(
//                                    this,
//                                    "Sorry, We can't add this one, with same remark (or) landmarks",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//
//                        }
//                    }else {
                        val addedissues: AddedIssuesDataModel = AddedIssuesDataModel()
                        addedissues.login_type = Main_prefence.getInstance().user_login_type
                        addedissues.username = Main_prefence.getInstance().user_first_name
                        addedissues.Image = Image_bitmap
                        addedissues.fetched_location = fetched_locations.text.toString()
                        addedissues.landmark = add_notes.text.toString()
                        addedissues.date_time = currentDateandTime
                        var img : String;
                        img=Bitmapconvert(Image_bitmap);
                        Main_prefence.getInstance().issuesdata.add(addedissues)
                        val databaseReference : DatabaseReference;
                        databaseReference=FirebaseDatabase.getInstance().getReference("Issues");
                        databaseReference.child(addedissues.fetched_location.toString()).child("User").setValue(email.toString())
                        databaseReference.child(addedissues.fetched_location.toString()).child("Image").setValue(img)
                        databaseReference.child(addedissues.fetched_location.toString()).child("Date").setValue(addedissues.date_time.toString())
                        databaseReference.child(addedissues.fetched_location.toString()).child("Land").setValue(addedissues.landmark.toString())
                        val a = Intent(this@AddIssuesActivity, UserMainActivity::class.java)
                        a.putExtra("User",value);
                        a.putExtra("Email",email);
                        a.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(a)
//                    }
                }
            }
        })

        fetch_location.setOnClickListener {
            checkPermissions()
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        currentDateandTime = sdf.format(Date())
        tv_date_time.text = currentDateandTime
    }

    private fun Bitmapconvert(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.DEFAULT)
    }


    private fun validateFields(): Boolean {
        var status: Boolean = true

        when {
            fetched_locations.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Press Location Icon to Fetch Current Locations",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }
            add_notes.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Enter Your Re-mark or Landmarks to avoid Repeat Entry",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }


            else -> {
                status = true
            }
        }
        return status
    }

    private fun openCamera() {

        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(
            intent, PERMISSION_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {

            if (requestCode == PERMISSION_IMAGE_CAPTURE) {

                try {
                    uri = data!!.getParcelableExtra<Uri>("path")!!
                    val myBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    encoded = getEncoded64ImageStringFromBitmap(myBitmap)!! //camera image
                    new_file_name = "Mobile_compliant"+ count + "file.jpg"

                    Image_bitmap = myBitmap
                    get_image = "yes"

                    uploaded_image.setImageBitmap(myBitmap)
                    BT_agree.text = "Save compliant"


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            else {
                Log.e("Test", "requestCode = $requestCode")
            }
        } else {
            Log.e("Test", "resultCode = $resultCode")
        }
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.DEFAULT)
    }

    private fun checkPermissions() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        Log.e("Test", "one fragment report.areAllPermissionsGranted() ture")
                        getLocation(applicationContext)
                    } else {
                        Log.e("Test", "one fragment report.areAllPermissionsGranted() false")
                        val location: LocationPopup =
                            LocationPopup(this@AddIssuesActivity)
                        location.showSettingsAlert()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    fun isLocationEnabledc(): Boolean {
        val locationManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun getLocation(context: Context) {
        if (isLocationEnabledc()) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            mFusedLocationClient.lastLocation.addOnCompleteListener {
                if (it.result != null) {

                    val location: Location? = it.result
                    if (location != null) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }

                    if (latitude != "" && longitude != "") {

                        val geocoder = Geocoder(context, Locale.getDefault())

                        try {
                            val list: List<Address> =
                                geocoder.getFromLocation(
                                    location!!.latitude,
                                    location!!.longitude, 1
                                )!!

                            address_line0 = list[0].getAddressLine(0).toString()
                            address_max = list[0].maxAddressLineIndex.toString()
                            pincode = list[0].postalCode
                            val str = list[0].getAddressLine(0).toString().split(",").toTypedArray()
                            address1 = str[0]
                            address2 = str[1]
                            area = list[0].locality
                            street = list[0].thoroughfare
                            state = list[0].adminArea

                            Log.e("Test", "address = $list")
                            Log.e("Test", "latitude = $latitude")
                            Log.e("Test", "longitude = $longitude")
                            Log.e("Test", "pincode = $pincode")
                            Log.e("Test", "street = $street")
                            Log.e("Test", "address_line0 = $address_line0")
                            Log.e("Test", "address_max = $address_max")
                            Log.e("Test", "address1 $address1 address2 $address2")

                            fetched_locations.text = address_line0.toString()

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            Log.e("Test", "catch")
                        }
                    } else {
                        Log.e("Test", "latitude && longitude")

                        /*Toast.makeText( requireActivity() as BaseActivity,
                            "can't fetch location",
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                } else {
                    Log.e("Test", "it.result")
                    Toast.makeText(
                        this,
                        "Could not fetch Location. Please try again",
                        Toast.LENGTH_LONG
                    ).show()

                    Handler(this.mainLooper).postDelayed({

                    }, 2000)

                }
            }

        } else {
            val intent1 = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(
                            this,
                            "Permission Granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}