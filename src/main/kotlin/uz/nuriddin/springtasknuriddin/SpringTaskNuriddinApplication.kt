package uz.nuriddin.springtasknuriddin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class SpringTaskNuriddinApplication

fun main(args: Array<String>) {
    runApplication<SpringTaskNuriddinApplication>(*args)
}
