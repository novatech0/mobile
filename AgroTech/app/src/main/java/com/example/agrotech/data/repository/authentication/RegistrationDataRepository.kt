package com.example.agrotech.data.repository.authentication

import javax.inject.Inject

class RegistrationDataRepository @Inject constructor() {
    var userId: Long = 0L
    var firstName: String = ""
    var lastName: String = ""
    var city: String = ""
    var country: String = ""
    var birthDate: String = ""
}
