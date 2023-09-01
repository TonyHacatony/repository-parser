package com.everamenkou.app.repository;

import com.everamenkou.app.repository.datasource.Repository;
import com.everamenkou.app.repository.datasource.github.GitHubDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {

    private final GitHubDataSource dataSource;

    @Autowired
    public RepositoryService(GitHubDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Repository> findRepositories(String username) {
        return dataSource.findPublicRepositories(username);
    }
}
