package com.everamenkou.app.repository.datasource.github;

import com.everamenkou.app.repository.datasource.Branch;
import com.everamenkou.app.repository.datasource.Repository;
import com.everamenkou.app.repository.datasource.RepositoryDataSource;
import com.everamenkou.app.repository.datasource.github.dto.GitHubBranchResponse;
import com.everamenkou.app.repository.datasource.github.dto.GitHubRepositoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class GitHubDataSource implements RepositoryDataSource {

    private final String REPOS_URL = "https://api.github.com/users/%s/repos";
    private final String BRANCH_URL = "https://api.github.com/repos/%s/%s/branches";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Repository> findPublicRepositories(String username) {
        ResponseEntity<GitHubRepositoryResponse[]> forEntity = restTemplate.getForEntity(getRepositoryUrl(username), GitHubRepositoryResponse[].class);
        List<GitHubRepositoryResponse> repositoryBody = Arrays.asList(Objects.requireNonNull(forEntity.getBody()));

        repositoryBody = repositoryBody.stream()
                .filter(repositoryResponse -> !repositoryResponse.fork())
                .toList();

        Map<String, List<Branch>> branchesByRepositoryName = findBranches(repositoryBody, username);
        return repositoryBody.stream()
                .map(repositoryResponse -> toRepository(repositoryResponse, branchesByRepositoryName))
                .collect(Collectors.toList());
    }

    private String getRepositoryUrl(String username) {
        return String.format(REPOS_URL, username);
    }

    private Map<String, List<Branch>> findBranches(List<GitHubRepositoryResponse> body, String username) {
        return body.stream()
                .map(GitHubRepositoryResponse::name)
                .collect(Collectors.toMap(repoName -> repoName, repoName -> getBranchesUrl(username, repoName)))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> findBranchesByRepository(entry.getValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().join()));
    }

    private String getBranchesUrl(String username, String branchName) {
        return String.format(BRANCH_URL, username, branchName);
    }

    private CompletableFuture<List<Branch>> findBranchesByRepository(String branchUrl) {
        return CompletableFuture.supplyAsync(() -> {
            GitHubBranchResponse[] branchResponses = restTemplate.getForObject(branchUrl, GitHubBranchResponse[].class);

            if (Objects.isNull(branchResponses)) {
                return Collections.emptyList();
            }

            return Arrays.stream(branchResponses)
                    .map(response -> new Branch(
                            response.name(),
                            response.commit().sha()
                    ))
                    .toList();
        });
    }

    private Repository toRepository(
            GitHubRepositoryResponse repository, Map<String,
            List<Branch>> branchesByRepositoryName
    ) {
        return new Repository(
                repository.name(),
                repository.owner().login(),
                branchesByRepositoryName.get(repository.name())
        );
    }
}
