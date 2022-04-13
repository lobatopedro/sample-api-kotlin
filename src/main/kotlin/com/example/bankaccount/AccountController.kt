package com.example.bankaccount

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(/* ...value = */ "accounts")
class AccountController(val repository: AccountRepository) {

    /*Jeito Java de fazer*/
    @PostMapping
    fun create0(@RequestBody account: Account): ResponseEntity<Account> {
        val accountSave = repository.save(account)
        return ResponseEntity.ok(accountSave)
    }

    /*Jeito Kotlin de fazer, bem mais simples*/
    @PostMapping
    fun create(@RequestBody account: Account) = ResponseEntity.ok(repository.save(account))

    @GetMapping
    fun read() = ResponseEntity.ok(repository.findAll())

    @PutMapping("{document}")
    fun update(@PathVariable document: String, @RequestBody account: Account) : ResponseEntity<Account> {
        val accountDBOptional = repository.findByDocument(document)
        val toSave = accountDBOptional
            .orElseThrow { RuntimeException("Account document: $document not found!!") }
            .copy(name = account.name, balance = account.balance)
        return ResponseEntity.ok(repository.save(toSave))

    }

    @DeleteMapping("{document}")
    fun delete(@PathVariable document: String) = repository
        .findByDocument(document)
        .ifPresent { repository.delete(it) }
}