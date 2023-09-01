package com.everamenkou.app.repository;

import com.everamenkou.app.repository.datasource.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitHubRepositoryController {

    private final RepositoryService service;

    @Autowired
    public GitHubRepositoryController(RepositoryService service) {
        this.service = service;
    }

    @GetMapping("/repos/{username}")
    List<Repository> findRepositories(@PathVariable String username) {
        return service.findRepositories(username);
    }
}
