package com.utn.ddam.cla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {
    lateinit var txtCartel : TextView
    lateinit var btnChangeText : Button
    lateinit var btnClearText : Button
    lateinit var btnRed : Button
    lateinit var btnGreen : Button
    lateinit var btnBlue : Button
    lateinit var btnSizeUp : Button
    lateinit var btnSizeDown : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCartel = findViewById(R.id.txtCartel);
        btnChangeText = findViewById(R.id.btnChangeText);
        btnClearText = findViewById(R.id.btnClearText);
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnSizeUp = findViewById(R.id.btnSizeUp);
        btnSizeDown = findViewById(R.id.btnSizeDown);

        btnChangeText.setOnClickListener() {
            if(txtCartel.text == "CURSANDO DDAM") {
                txtCartel.text = "Desarrollo de Aplicaciones Mobile";
            } else {
                txtCartel.text = "CURSANDO DDAM";
            }
        }

        btnClearText.setOnClickListener() {
            txtCartel.text = "";
        }

        btnRed.setOnClickListener() {
            txtCartel.setTextColor("#FF0000".toColorInt());
        }

        btnGreen.setOnClickListener() {
            txtCartel.setTextColor("#00FF00".toColorInt());
        }

        btnBlue.setOnClickListener() {
            txtCartel.setTextColor("#0000FF".toColorInt());
        }

        btnSizeUp.setOnClickListener() {
            txtCartel.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtCartel.textSize + 5)
        }

        btnSizeDown.setOnClickListener() {
            txtCartel.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtCartel.textSize - 5)
        }
    }
}