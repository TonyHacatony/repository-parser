package com.everamenkou.app.repository.datasource;

import java.util.List;
/*
* Used for collecting data from the api
* */
public interface RepositoryDataSource {

    List<Repository> findPublicRepositories(String username);
}
