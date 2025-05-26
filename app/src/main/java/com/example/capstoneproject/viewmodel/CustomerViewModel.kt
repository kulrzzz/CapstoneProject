package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.launch
import android.util.Log

class CustomerViewModel : ViewModel() {

    // State untuk daftar customer
    var customerList by mutableStateOf<List<Customer>>(emptyList())
        private set

    // State untuk status loading
    var isLoading by mutableStateOf(false)
        private set

    // State untuk pesan error (jika ada)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Ambil access token dari konfigurasi BuildConfig.
     * Gantilah dengan sistem token dinamis jika menggunakan login user.
     */
    private val token: String
        get() = BuildConfig.API_ACCESS_TOKEN

    /**
     * Fungsi untuk mengambil semua data customer dari API.
     */
    fun fetchAllCustomers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // API Anda langsung mengembalikan List<Customer>
                val result = ApiClient.customerService.getAllCustomers(token)
                customerList = result
            } catch (e: Exception) {
                Log.e("CustomerViewModel", "Fetch failed", e)
                errorMessage = "Gagal memuat data customer: ${e.localizedMessage ?: "Tidak diketahui"}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Mengambil data customer berdasarkan ID.
     * Berguna untuk menampilkan detail user.
     */
    fun getCustomerById(id: String): Customer? {
        return customerList.find { it.customer_id == id }
    }

    fun setError(msg: String) {
        errorMessage = msg
    }
}