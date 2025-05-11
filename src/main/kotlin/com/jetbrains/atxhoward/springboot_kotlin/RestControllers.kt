package com.jetbrains.atxhoward.springboot_kotlin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/articles")
class ArticleController(val repository: ArticleRepository) {

    @GetMapping
    fun articles() = repository.findAllByOrderByCreatedAtDesc()

    @GetMapping("/{slug}")
    fun article(@PathVariable slug: String) =
        repository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

    @PostMapping
    fun newArticle(@RequestBody article: Article): Article {
        article.id = null
        repository.save(article)
        return article
    }

    @PutMapping("/{slug}")
    fun updateArticle(@PathVariable slug: String, @RequestBody article: Article): Article {
        val existingArticle = repository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        existingArticle.content = article.content
        repository.save(existingArticle)
        return article
    }

    @DeleteMapping("/{slug}")
    fun deleteArticle(@PathVariable slug: String){
        val existingArticle = repository.findBySlug(slug).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        repository.delete(existingArticle)
    }
}