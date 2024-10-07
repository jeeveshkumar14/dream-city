package com.example.splash_screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

var autoCompleteTextView: AutoCompleteTextView? = null



val item = arrayOf("Citizen", "Service Worker", "Municipality")



class RegisterActivity : AppCompatActivity() {

    lateinit var edtSignUpFullName: TextInputEditText
    lateinit var edtSignUpEmail: TextInputEditText
    lateinit var edtSignUpMobile: TextInputEditText
    lateinit var edtSignUpPassword: TextInputEditText
    lateinit var edtSignUpConfirmPassword: TextInputEditText

    lateinit var btnSignUp: Button
    lateinit var txtSignIn: TextView;
    lateinit var firebase : FirebaseDatabase;
    lateinit var database : DatabaseReference;
    var value = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val extras = intent.extras
        if (extras != null) {
            value = extras.getString("User").toString()
        }

//
//        autoCompleteTextView=findViewById(R.id.options);



        edtSignUpFullName = findViewById<View>(R.id.edtSignUpFullName) as TextInputEditText
        edtSignUpEmail = findViewById<View>(R.id.edtSignUpEmail) as TextInputEditText
        edtSignUpMobile = findViewById<View>(R.id.edtSignUpMobile) as TextInputEditText
        edtSignUpPassword = findViewById<View>(R.id.edtSignUpPassword) as TextInputEditText
        edtSignUpConfirmPassword = findViewById<View>(R.id.edtSignUpConfirmPassword) as TextInputEditText

        btnSignUp = findViewById<View>(R.id.btnSignUp) as Button
        txtSignIn = findViewById<View>(R.id.txtSignIn) as TextView
        firebase=FirebaseDatabase.getInstance();
        database=firebase.getReference(value);
        btnSignUp.setOnClickListener {
            if (validateFields()) {

                Main_prefence.getInstance().user_first_name = edtSignUpFullName.getText().toString()
                Main_prefence.getInstance().user_login_type = value
                Main_prefence.getInstance().user_login = "Login"

                val registerdata: RegisterDataModel = RegisterDataModel()
                registerdata.login_type = value
                registerdata.username = edtSignUpFullName.text.toString()
                registerdata.email_id = edtSignUpEmail.text.toString()
                registerdata.mobile_number = edtSignUpMobile.text.toString()
                registerdata.password = edtSignUpPassword.text.toString()
                database.child(registerdata.mobile_number.toString()).child("Name").setValue(registerdata.username.toString());
                database.child(registerdata.mobile_number.toString()).child("Email").setValue(registerdata.email_id.toString());
                database.child(registerdata.mobile_number.toString()).child("Pass").setValue(registerdata.password.toString());
                database.child(registerdata.mobile_number.toString()).child("Count").setValue("0");
                database.child(registerdata.mobile_number.toString()).child("Solve").setValue("0");
                database.child(registerdata.mobile_number.toString()).child("Work").setValue("0");
//                Main_prefence.getInstance().registerdata.add(registerdata)


                val activity2Intent = Intent(this@RegisterActivity, UserMainActivity::class.java)
                activity2Intent.putExtra("User", value)
                startActivity(activity2Intent)
            }
        }

        txtSignIn.setOnClickListener {
           onBackPressed()
        }


    }

    private fun validateFields(): Boolean {
        var status: Boolean = true

        when {
            edtSignUpFullName.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Enter Full Name",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }
            edtSignUpEmail.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Enter Your mail Id Name",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }
            edtSignUpMobile.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Enter Mobile Number",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }
            edtSignUpPassword.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Enter Password",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }
            edtSignUpConfirmPassword.text.toString() == "" -> {
                Toast.makeText(
                    this,
                    "Select Confirm Password",
                    Toast.LENGTH_LONG
                ).show()
                status = false
            }

            edtSignUpPassword.text.toString() != edtSignUpConfirmPassword.text.toString() -> {
                Toast.makeText(
                    this,
                    "Select Password and Confirm Password Not Matching",
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

}