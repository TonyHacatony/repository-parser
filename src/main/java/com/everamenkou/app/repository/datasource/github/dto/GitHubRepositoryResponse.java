package com.everamenkou.app.repository.datasource.github.dto;

public record GitHubRepositoryResponse(
        String name,
        boolean fork,
        GithubOwner owner
) {}