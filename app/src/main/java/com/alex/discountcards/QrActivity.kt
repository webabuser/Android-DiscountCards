package com.alex.discountcards

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

class QrActivity : AppCompatActivity() {

    private lateinit var qrImageView: ImageView
    private lateinit var qrStoreName: TextView
    private lateinit var qrCardNumber: TextView
    private lateinit var backButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        qrImageView = findViewById(R.id.qrImageView)
        qrStoreName = findViewById(R.id.qrStoreName)
        qrCardNumber = findViewById(R.id.qrCardNumber)
        backButton = findViewById(R.id.backButton)

        // Получаем данные из Intent
        val storeName = intent.getStringExtra("STORE_NAME") ?: "Магазин"
        val cardNumber = intent.getStringExtra("CARD_NUMBER") ?: "0000 0000"
        val codeTypeName = intent.getStringExtra("CODE_TYPE") ?: "QR"
        val codeType = CodeType.valueOf(codeTypeName)

        // Отображаем текст
        qrStoreName.text = storeName
        qrCardNumber.text = "Номер: $cardNumber"

        // Генерируем QR-код
        generateCode(cardNumber, codeType)

        // Кнопка назад
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun generateCode(text: String, type: CodeType) {
        val format = when (type) {
            CodeType.QR -> BarcodeFormat.QR_CODE
            CodeType.EAN13 -> BarcodeFormat.EAN_13
            CodeType.EAN8 -> BarcodeFormat.EAN_8
            CodeType.CODE128 -> BarcodeFormat.CODE_128
        }

        val width = if (type == CodeType.QR) 512 else 800
        val height = if (type == CodeType.QR) 512 else 300

        val writer = MultiFormatWriter()
        val bitMatrix = writer.encode(text, format, width, height)


       // val writer = QRCodeWriter()
        try {
            //val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val bitMatrix = writer.encode(text, format, width, height)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            qrImageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}