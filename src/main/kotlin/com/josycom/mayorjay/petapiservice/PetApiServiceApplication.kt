package com.josycom.mayorjay.petapiservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetApiServiceApplication

fun main(args: Array<String>) {
	runApplication<PetApiServiceApplication>(*args)
}
