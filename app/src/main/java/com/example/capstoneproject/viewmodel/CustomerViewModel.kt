package com.example.capstoneproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.Customer
import com.example.capstoneproject.network.ApiClient
import com.example.capstoneproject.util.Constants
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class CustomerViewModel : ViewModel() {

    var customerList by mutableStateOf<List<Customer>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchAllCustomers() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = ApiClient.apiService.getAllCustomers(Constants.ACCESS_TOKEN)
                customerList = response.data // pastikan response JSON punya `data`
            } catch (e: Exception) {
                errorMessage = "Gagal memuat customer: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getCustomerById(id: String): Customer? {
        return customerList.find { it.customer_id == id }
    }
}