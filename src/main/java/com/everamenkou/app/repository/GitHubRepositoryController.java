package com.everamenkou.app.repository;

import com.everamenkou.app.error.ApiError;
import com.everamenkou.app.repository.datasource.RepositoryDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitHubRepositoryController {

    private final RepositoryDataSource service;

    @Autowired
    public GitHubRepositoryController(RepositoryDataSource service) {
        this.service = service;
    }

    @GetMapping("/repos/{username}")
    ResponseEntity<?> findRepositories(
            @RequestHeader(HttpHeaders.ACCEPT) List<String> acceptHeader,
            @PathVariable String username
    ) {
        if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)
                || acceptHeader.contains(MediaType.ALL_VALUE)) {

            return new ResponseEntity<>(service.findPublicRepositories(username), HttpStatus.OK);
        }

        String msg = "Incorrect media type, we support only ".concat(MediaType.APPLICATION_JSON.toString());
        return new ResponseEntity<>(new ApiError(msg, HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE);
    }
}
