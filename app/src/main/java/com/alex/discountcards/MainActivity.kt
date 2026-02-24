package com.alex.discountcards

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var emptyText: TextView
    private val cards = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.cardsListView)
        addButton = findViewById(R.id.addButton)
        emptyText = findViewById(R.id.emptyText)

        // cards.add(Card(1, "Starbucks", "5901234123457", 15))
        // cards.add(Card(2, "Ашан", "1234567890123", 10))
        // cards.add(Card(3, "Пятерочка", "1234567890122", 5))

        // 8. Загружаем сохраненные карты
        cards.clear()
        cards.addAll(CardStorage.loadCards(this))

        // Используем адаптер
        val adapter = CardAdapter(this, cards)
        listView.adapter = adapter

        println("DEBUG: Карт в списке: ${cards.size}")

        // Показываем/скрываем пустой текст
        if (cards.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }

        // Кнопка добавления
        addButton.setOnClickListener {
            startActivity(Intent(this, AddCardActivity::class.java))
        }

        // Клик по элементу списка
        listView.setOnItemClickListener { _, _, position, _ ->
            val card = cards[position]

            /*/ Временно покажи Toast вместо перехода
            Toast.makeText(
                this,
                "Клик на позиции: $position\nКарта: ${cards[position].storeName}",
                Toast.LENGTH_LONG
            ).show()
            */


            val intent = Intent(this, QrActivity::class.java).apply {
                putExtra("STORE_NAME", card.storeName)
                putExtra("CARD_NUMBER", card.cardNumber)
                putExtra("CODE_TYPE", card.codeType.name)
            }
            startActivity(intent)
            

        }
    }



    override fun onResume() {
        super.onResume()

        // Перезагружаем список карт
        cards.clear()
        cards.addAll(CardStorage.loadCards(this))

        // Обновляем адаптер
        val adapter = CardAdapter(this, cards)
        listView.adapter = adapter

        // Показываем/скрываем пустой текст
        if (cards.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }


}

// Адаптер ВНЕ класса MainActivity
class CardAdapter(
    context: Context,
    private val cards: List<Card>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = cards.size
    override fun getItem(position: Int): Card = cards[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_card, parent, false)

        view.findViewById<TextView>(R.id.storeName).text = cards[position].storeName
        view.findViewById<TextView>(R.id.cardNumber).text = "Номер: ${cards[position].cardNumber}"
        view.findViewById<TextView>(R.id.discountInfo).text = "Скидка: ${cards[position].discount}%"
        view.findViewById<TextView>(R.id.descriptionInfo).text =
            cards[position].description.ifEmpty { "Нет описания" }

        return view
    }
}