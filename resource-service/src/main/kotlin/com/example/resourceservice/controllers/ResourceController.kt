package com.example.resourceservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResourceController {

    @GetMapping("open")
    fun open(): String {
        return "Hello open endpoint"
    }

    @GetMapping("closed")
    fun closed(): String {
        return "Hello closed endpoint"
    }
}