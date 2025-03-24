package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var baseAmountEditText: EditText
    private lateinit var multiplierSeekBar: SeekBar
    private lateinit var multiplierValue: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var ratingText: TextView
    private lateinit var tipAmount: TextView
    private lateinit var tipPercentage: TextView

    private var multiplier: Double = 0.0
    private var total: Double = 0.0
    private var base: Double = 0.0
    private var tip: Double = 0.0
    private var baseTip: Double = 5.0
    private var radioTip: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.numberInput)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        baseAmountEditText = findViewById(R.id.baseAmount)
        multiplierSeekBar = findViewById(R.id.ratingBar)
        totalAmountTextView = findViewById(R.id.totalAmount)
        ratingText = findViewById(R.id.ratingText)
        tipAmount = findViewById(R.id.tipAmount)
        tipPercentage = findViewById(R.id.tipPercentage)


        multiplierSeekBar.max = 10

        multiplierSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                Log.i(TAG, "onProgressChanged $progress")
                ratingText.text = "$progress"
                multiplier = progress.toDouble()
                calculateTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        baseAmountEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    base = 0.0
                    calculateTotal()
                }
                else{
                    base = s.toString().toDouble()
                    calculateTotal()
                }
            }

        })

        findViewById<RadioButton>(R.id.radioGood).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioTip = 5.0
                calculateTotal()
            }
        }
        findViewById<RadioButton>(R.id.radioMid).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioTip = 2.0
                calculateTotal()
            }
        }
        findViewById<RadioButton>(R.id.radioBad).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioTip = 0.0
                calculateTotal()
            }
        }
    }

    private fun calculateTotal(){
        tip = (baseTip + multiplier + radioTip- 5) / 100 * base
        total = base + tip
        val tipFormat = String.format(Locale.getDefault(), "%.2f", tip)
        val percentage = (baseTip + multiplier + radioTip - 5)
        totalAmountTextView.text = "$total"
        tipAmount.text = "$tipFormat zł"
        tipPercentage.text = "$percentage%"
    }
}