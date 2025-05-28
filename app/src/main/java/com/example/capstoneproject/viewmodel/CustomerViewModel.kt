package com.example.capstoneproject.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.booking.BookingDetail
import com.example.capstoneproject.model.customer.Customer
import com.example.capstoneproject.model.customer.CustomerDeleteRequest
import com.example.capstoneproject.network.ApiClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CustomerViewModel : ViewModel() {

    private val _customerList = mutableStateListOf<Customer>()
    val customerList: SnapshotStateList<Customer> get() = _customerList

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    private val _deleteSuccess = mutableStateOf<Boolean?>(null)
    val deleteSuccess: State<Boolean?> get() = _deleteSuccess

    private val _selectedCustomer = mutableStateOf<Customer?>(null)
    val selectedCustomer: State<Customer?> get() = _selectedCustomer

    private val _customerBookings = mutableStateOf<List<BookingDetail>>(emptyList())
    val customerBookings: State<List<BookingDetail>> get() = _customerBookings

    fun fetchCustomers(token: String, retryCount: Int = 3) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repeat(retryCount) { attempt ->
                try {
                    val result = withContext(Dispatchers.IO) {
                        ApiClient.customerService.getAllCustomers(token)
                    }
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

    fun fetchCustomerById(id: String, token: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                println("Fetching customer with id: $id")
                val result = withContext(Dispatchers.IO) {
                    ApiClient.customerService.getCustomerDetail(id, token)
                }
                _selectedCustomer.value = result
            } catch (e: CancellationException) {
                println("Coroutine fetchCustomerById dibatalkan: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat detail customer: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCustomerBookings(bookings: List<BookingDetail>) {
        _customerBookings.value = bookings
    }

    fun deleteCustomer(accessToken: String, customerId: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            _deleteSuccess.value = null

            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.customerService.deleteCustomer(
                        CustomerDeleteRequest(access_token = accessToken, customer_id = customerId)
                    )
                }
                if (response.isSuccessful) {
                    _customerList.removeAll { it.customer_id == customerId }
                    _deleteSuccess.value = true
                } else {
                    _errorMessage.value = "Gagal menghapus user: ${response.code()}"
                    _deleteSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
                _deleteSuccess.value = false
            }
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

    fun resetDeleteState() {
        _deleteSuccess.value = null
    }
}