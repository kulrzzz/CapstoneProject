package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {

    private val _customerList = mutableStateListOf<Customer>()
    val customerList: SnapshotStateList<Customer> get() = _customerList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    fun fetchCustomers(token: String, retryCount: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repeat(retryCount) { attempt ->
                try {
                    val result = ApiClient.customerService.getAllCustomers(token)
                    _customerList.clear()
                    _customerList.addAll(result)
                    _isLoading.value = false
                    return@launch
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (attempt == retryCount - 1) {
                        _errorMessage.value = "Gagal memuat data customer: ${e.localizedMessage}"
                    } else {
                        delay(500)
                    }
                }
            }

            _isLoading.value = false
        }
    }

    fun refreshCustomers(token: String) {
        fetchCustomers(token)
    }

    fun getCustomerById(id: String): Customer? {
        return _customerList.find { it.customer_id == id }
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }
}