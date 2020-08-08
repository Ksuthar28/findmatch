package com.saadi.findmatch.utils;

public class Constants {

    /**
     * Created by Kailash Suthar.
     */

    public static class URL {
        public static final String GET_MATCHES_LIST = "https://randomuser.me/api/?results=10";
    }

    public static class RequestTags {
        public static final String RT_FIND_MATCH = "find_match";
    }

    public static class RetryPolicy {
        public static final int TIMEOUT_MS = 30000;//Volley default timeout 2500
        public static final int MAX_RETRIES = 0;//Volley default maximum retries 1
    }

    public static class Strings {
        public static final String SEVER_UNREACHABLE = "The server is unreachable";
        public static final String NO_INTERNET = "The internet connection appears to be offline";
        public static final String CONNECTION_TIMEOUT = "The server is taking to long to respond";
    }
}
