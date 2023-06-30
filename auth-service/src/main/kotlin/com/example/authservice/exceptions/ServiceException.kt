package com.example.authservice.exceptions

import java.lang.Exception

class ServiceException(exception: Exception): RuntimeException(exception) {
}