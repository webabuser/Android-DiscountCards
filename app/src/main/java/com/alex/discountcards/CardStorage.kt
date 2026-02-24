package com.alex.discountcards

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CardStorage {
    private const val PREFS_NAME = "card_prefs"
    private const val CARDS_KEY = "cards_list"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Сохранить список карт
    fun saveCards(context: Context, cards: List<Card>) {
        val gson = Gson()
        val json = gson.toJson(cards)
        getPrefs(context).edit().putString(CARDS_KEY, json).apply()
    }

    // Загрузить список карт
    fun loadCards(context: Context): List<Card> {
        val gson = Gson()
        val json = getPrefs(context).getString(CARDS_KEY, "") ?: ""

        return if (json.isEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<Card>>() {}.type
            gson.fromJson(json, type)
        }
    }

    // Очистить все карты (для отладки)
    fun clearCards(context: Context) {
        getPrefs(context).edit().remove(CARDS_KEY).apply()
    }
}