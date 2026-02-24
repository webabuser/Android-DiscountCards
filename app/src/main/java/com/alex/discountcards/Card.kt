package com.alex.discountcards



enum class CodeType {
    QR,        // QR-код
    EAN13,     // Штрих-код 13 цифр
    EAN8,      // Штрих-код 8 цифр
    CODE128    // Универсальный штрих-код
}

data class Card(
    val id: Int = 0,
    val storeName: String,
    val cardNumber: String,
    val discount: Int = 0,
    val description: String = "",
    val codeType: CodeType = CodeType.QR
)
