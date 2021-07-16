package ru.ws.utils;

public class RestURIConstants {

    private static final String BASE_AUTHENTICATED_URL = "/secure/";
    private static final String BASE_PUBLIC_URL = "/public/";

    public static final String POST_LOGIN = BASE_PUBLIC_URL + "authentication/login";

    public static final String POST_USER = BASE_PUBLIC_URL + "user";
    public static final String GET_USER_BY_ID = BASE_AUTHENTICATED_URL + "user/{id}";
    public static final String DELETE_USER_BY_ID = BASE_AUTHENTICATED_URL + "user/{id}";
    public static final String GET_USER_LIST = BASE_AUTHENTICATED_URL + "user/detail/userList";
    public static final String GET_USER__ARTICLE_LIST = BASE_AUTHENTICATED_URL + "user/detail/userArticleList";  
}
