package com.myou.sample.jakarta.webapp.backingbean;

import com.myou.sample.jakarta.webapp.entity.UserEntity;
import com.myou.sample.jakarta.webapp.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named("userSearch")
@RequestScoped
public class UserSearchBean {

    private String searchQuery;
    private List<UserEntity> searchResults;

    @Inject
    private UserService userService; // UserServiceはユーザー検索メソッドも提供する必要があります。

    // ゲッターとセッター

    public void search() {
        searchResults = userService.getAllEntities();
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

}
