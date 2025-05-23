package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.Customer
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {

    var customerList by mutableStateOf<List<Customer>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Ambil token dari konfigurasi build. Bisa diganti runtime kalau ada login dinamis.
     */
    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    /**
     * Ambil seluruh data customer dari server.
     */
    fun fetchAllCustomers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = ApiClient.apiService.getAllCustomers("Bearer $token")
                customerList = response.data
            } catch (e: Exception) {
                errorMessage = "Gagal memuat customer: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Ambil data customer berdasarkan ID.
     */
    fun getCustomerById(id: String): Customer? {
        return customerList.find { it.customer_id == id }
    }
}