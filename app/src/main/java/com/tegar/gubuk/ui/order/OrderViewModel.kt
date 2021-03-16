package com.tegar.gubuk.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tegar.gubuk.firestore.OrderFireStore
import com.tegar.gubuk.model.Order
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val _grouping = MutableLiveData<List<Order>>()
    val grouping: LiveData<List<Order>> get() = _grouping

    fun getOrderById(idUser: String) = viewModelScope.launch {
        OrderFireStore.getOrderById(idUser) {
            _grouping.value = it
        }
    }
}