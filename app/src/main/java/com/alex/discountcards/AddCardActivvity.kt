package com.alex.discountcards

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView

class AddCardActivity : AppCompatActivity() {

    private lateinit var storeNameEdit: EditText
    private lateinit var cardNumberEdit: EditText
    private lateinit var discountEdit: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var descriptionEdit: EditText
    private lateinit var codeTypeSpinner: AppCompatSpinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Находим View
        storeNameEdit = findViewById(R.id.storeNameEdit)
        cardNumberEdit = findViewById(R.id.cardNumberEdit)
        discountEdit = findViewById(R.id.discountEdit)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        descriptionEdit = findViewById(R.id.descriptionEdit)
        codeTypeSpinner = findViewById(R.id.codeTypeSpinner)

        // === НАСТРОЙКА SPINNER ===
        val codeTypes = arrayOf("QR", "EAN-13", "EAN-8", "CODE-128")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, codeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        codeTypeSpinner.adapter = adapter
        codeTypeSpinner.setSelection(0)

        codeTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@AddCardActivity, "Выбрано: ${codeTypes[position]}", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // ===========================

        // Кнопка "Сохранить"
        saveButton.setOnClickListener {
            val storeName = storeNameEdit.text.toString()
            val cardNumber = cardNumberEdit.text.toString()
            val discount = discountEdit.text.toString().toIntOrNull() ?: 0
            val description = descriptionEdit.text.toString()

            val codeType = when (codeTypeSpinner.selectedItemPosition) {
                0 -> CodeType.QR
                1 -> CodeType.EAN13
                2 -> CodeType.EAN8
                else -> CodeType.CODE128
            }

            // Проверка...
            if (storeName.isBlank() || cardNumber.isBlank()) {
                Toast.makeText(this, "Заполните название и номер карты", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newCard = Card(
                id = System.currentTimeMillis().toInt(),
                storeName = storeName,
                cardNumber = cardNumber,
                discount = discount,
                description = description,
                codeType = codeType
            )

            val currentCards = CardStorage.loadCards(this).toMutableList()
            currentCards.add(newCard)
            CardStorage.saveCards(this, currentCards)

            Toast.makeText(this, "Карта добавлена", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Кнопка "Отмена"
        cancelButton.setOnClickListener {
            finish()
        }
    }
}