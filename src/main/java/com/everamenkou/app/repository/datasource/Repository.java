package com.everamenkou.app.repository.datasource;

import java.util.List;

public record Repository(String name, String owner, List<Branch> branches) {}
