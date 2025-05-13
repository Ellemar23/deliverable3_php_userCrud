package com.example.deliverable3_php_usercrud;

public class endpoints {
    private static final String BASE_URL = "http://192.168.0.107/FinalProject/Final%20act/"; // Replace with your WAMP IP & folder


    public static final String CREATE = BASE_URL + "user.php?action=create";
    public static final String READ_ALL = BASE_URL + "user.php?action=read_all";
    public static final String READ_ONE = BASE_URL + "user.php?action=read_one";
    public static final String UPDATE = BASE_URL + "user.php?action=update";
    public static final String DELETE = BASE_URL + "user.php?action=delete";
}

