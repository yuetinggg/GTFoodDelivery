package com.example.apple.gtdelivery;

/**
 * Created by Venkat on 4/24/16.
 */
public class Constants {
    public static final String NOTHING = "N";
    public static final String ORDER_REQUESTED = "O";
    public static final String ORDER_ACCEPTED = "A";
    public static final String ORDER_DELIVERED = "D";
    public static final String STRIPE_API_TEST_PUBLISHABLE_KEY = "pk_test_0KPlH4Hw2FUzR7bakDWXZqr1";
    public static final String STRIPE_API_TEST_SECRET_KEY = "sk_test_b2utQtXmanzlaZXruj50NJ3i";
    public static final String BASE_URL = "https://gtfood.firebaseio.com/";

    // USER SCHEMA
    public static final String USER_EMAIL = "email";
    public static final String USER_NAME = "name";
    public static final String USER_RATING = "rating";
    public static final String USER_STATUS = "status";
    public static final String USER_STRIPE_ID = "customer_stripe_id";
    public static final String USER_STRIPE_ID_DEFAULT = "notSet";
    public static final String USER_NUM_RATINGS = "num_ratings";

    //FONTS
    public static final String COMIC_FONT = "fonts/BADABB.ttf";
    public static final String NORMAL_FONT = "fonts/YanoneKaffeesatz-Regular.ttf";

}
