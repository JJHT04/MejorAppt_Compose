package com.mejorappt.equipoa.db

interface DAO<K, E> {

    fun get(id: K): E?

    fun getAll(): List<E>

    fun update(values: List<E>)

    fun update(value: E) = update(listOf(value))

    fun insert(values: List<E>)

    fun insert(value: E) = insert(listOf(value))
}