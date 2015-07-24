package ua.org.javatraining.automessenger.app.database;

public interface DbConstants {
    String MYDATABASE_NAME = "autoMessenger";
    int MYDATABASE_VERSION = 1;
    //Запросы создания таблиц
    String USER_CREATE = "create table IF NOT EXISTS USER (USER_NAME text primary key not null unique);";
    String TAG_CREATE = "create table IF NOT EXISTS TAG (TAG_NAME text primary key);";
    String SUBSCRIPTION_CREATE = "create table IF NOT EXISTS SUBSCRIPTION (ID integer primary key not null unique, USER_NAME text, TAG_NAME text, CONSTRAINT fk_subscription_user_parent FOREIGN KEY (USER_NAME) REFERENCES user (USER_NAME) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT fk_subscription_tag_parent FOREIGN KEY (TAG_NAME) REFERENCES tag (TAG_NAME) ON DELETE CASCADE ON UPDATE CASCADE);";
    String POST_CREATE = "create table IF NOT EXISTS POST (ID integer primary key autoincrement not null unique, POST_TEXT text, POST_DATE integer, POST_LOCATION text, USER_NAME text, TAG_NAME text, LOC_COUNTRY text, LOC_ADMIN_AREA text, LOC_REGION text, CONSTRAINT fk_post_user_parent\n" +
            "\tFOREIGN KEY (USER_NAME)\n" +
            "\tREFERENCES USER (USER_NAME)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE,\n" +
            " CONSTRAINT fk_post_tag_parent\n" +
            "\tFOREIGN KEY (TAG_NAME)\n" +
            "\tREFERENCES TAG (TAG_NAME)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE);";
    String COMMENT_CREATE = "create table IF NOT EXISTS COMMENT (ID integer primary key autoincrement not null unique, COMMENT_DATE integer not null, COMMENT_TEXT text, USER_NAME text, ID_POST integer, CONSTRAINT fk_comment_user_parent\n" +
            "    FOREIGN KEY (USER_NAME)\n" +
            "    REFERENCES USER (USER_NAME)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "\n" +
            "    CONSTRAINT fk_comment_post_parent\n" +
            "    FOREIGN KEY (id_post)\n" +
            "    REFERENCES `post`(id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String GRADE_POST_CREATE = "create table IF NOT EXISTS GRADE_POST (ID integer primary key not null, USER_NAME text, ID_POST integer, GRADE integer, CONSTRAINT fk_grade_post_user_parent\n" +
            "    FOREIGN KEY (USER_NAME)\n" +
            "    REFERENCES USER (USER_NAME)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "\n" +
            "    CONSTRAINT fk_grade_post_comment_parent\n" +
            "    FOREIGN KEY (id_post)\n" +
            "    REFERENCES COMMENT (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String GRADE_COMMENT_CREATE = "create table IF NOT EXISTS GRADE_COMMENT (ID integer primary key autoincrement not null unique, USER_NAME text, ID_COMMENT integer, GRADE integer,  CONSTRAINT fk_grade_comment_user_parent\n" +
            "    FOREIGN KEY (USER_NAME)\n" +
            "    REFERENCES USER (USER_NAME)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "    CONSTRAINT fk_grade_comment_comment_parent\n" +
            "    FOREIGN KEY (id_comment)\n" +
            "    REFERENCES comment (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String PHOTO_CREATE = "create table IF NOT EXISTS PHOTO (ID integer primary key autoincrement not null unique, LINK text, ID_POST integer,  CONSTRAINT fk_photo_post_parent\n" +
            "\tFOREIGN KEY (id_post)\n" +
            "\tREFERENCES POST (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE)";
    //Константы для таблиц
    String ID = "ID";
    String USER_TABLE = "USER";
    String USER_NAME = "USER_NAME";

    String TAG_TABLE = "TAG";
    String TAG_NAME = "TAG_NAME";

    String SUBSCRIPTION_TABLE = "SUBSCRIPTION";

    String POST_TABLE = "POST";
    String POST_TEXT = "POST_TEXT";
    String POST_DATE = "POST_DATE";
    String POST_LOCATION = "POST_LOCATION";
    String LOC_COUNTRY = "LOC_COUNTRY";
    String LOC_ADMIN_AREA = "LOC_ADMIN_AREA";
    String LOC_REGION = "LOC_REGION";

    String COMMENT_TABLE = "COMMENT";
    String COMMENT_DATE = "COMMENT_DATE";
    String COMMENT_TEXT = "COMMENT_TEXT";
    String ID_POST = "ID_POST";

    String GRADE_POST_TABLE = "GRADE_POST";
    String GRADE = "GRADE";

    String GRADE_COMMENT_TABLE = "GRADE_COMMENT";
    String ID_COMMENT = "ID_COMMENT";

    String PHOTO_TABLE = "PHOTO";
    String LINK = "LINK";

    //Select запросы
    String QUERY_USER = "SELECT " + USER_NAME + " from " + USER_TABLE + " where " + USER_NAME + " = ?";
    String QUERY_TAG_BY_ID = "SELECT " + TAG_NAME + " from " + TAG_TABLE + " where " + ID + " = ?";
    String QUERY_SEARCH_TAG = "SELECT " + TAG_NAME + " FROM " + TAG_TABLE + " WHERE " + TAG_NAME + " LIKE ?";
    String QUERY_COMMENT_COUNT_BY_POST_ID = "SELECT COUNT(*) FROM " + COMMENT_TABLE + " WHERE " + ID_POST + " = ?";
    String QUERY_POST_BY_SUBSCRIPTIONS = "SELECT * FROM " + POST_TABLE + " WHERE " + USER_NAME + " = ? AND " + TAG_NAME + " IN (" +
            " SELECT " + TAG_NAME + " FROM " + SUBSCRIPTION_TABLE + " WHERE " + USER_NAME + " = ? )";
    String QUERY_POST_BY_SUBSCRIPTIONS_NEXT_PAGE = "SELECT * FROM " + POST_TABLE + " WHERE " + USER_NAME + " = ? AND " + TAG_NAME + " IN (" +
            " SELECT " + TAG_NAME + " FROM " + SUBSCRIPTION_TABLE + " WHERE " + USER_NAME + " = ? ) AND " + POST_DATE + " < ?";
    String QUERY_POSTS_BY_LOCATION_ONE_WORD = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ?";
    String QUERY_POSTS_BY_LOCATION_TWO_WORDS = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ? AND " + LOC_ADMIN_AREA + " = ?";
    String QUERY_POSTS_BY_LOCATION_THREE_WORDS = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ? AND " + LOC_ADMIN_AREA + " = ? AND " + LOC_REGION + " = ?";
    String QUERY_POSTS_BY_LOCATION_ONE_WORD_NEXT_PAGE = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ? AND " + POST_DATE + " < ?";
    String QUERY_POSTS_BY_LOCATION_TWO_WORDS_NEXT_PAGE = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ? AND " + LOC_ADMIN_AREA + " = ? AND " + POST_DATE + " < ?";
    String QUERY_POSTS_BY_LOCATION_THREE_WORDS_NEXT_PAGE = "SELECT * FROM " + POST_TABLE + " WHERE " + LOC_COUNTRY + " = ? AND " + LOC_ADMIN_AREA + " = ? AND " + LOC_REGION + " = ? AND " + POST_DATE + " < ?";
    String QUERY_ALL_TAG_ID_BY_USER_NAME = "SELECT " + "*" + " from " + SUBSCRIPTION_TABLE + " where " + USER_NAME + " = ?";
    String QUERY_ALL_POST_BY_USER_NAME_AND_TAG_NAME = "SELECT " + "*" + " from " + POST_TABLE + " where " + USER_NAME + " = ?" + " and " + TAG_NAME + " = ?";
    String QUERY_ALL_POST_BY_TAG_NAME = "SELECT " + "*" + " from " + POST_TABLE + " where " + TAG_NAME + " = ?";
    String QUERY_ALL_POST_BY_TAG_NAME_NEXT_PAGE = "SELECT * FROM " + POST_TABLE + " WHERE " + TAG_NAME + " = ? AND " + POST_DATE + " < ?";
    String QUERY_ALL_POST_BY_USER_NAME = "SELECT " + "*" + " from " + POST_TABLE + " where " + USER_NAME + " = ?";
    String QUERY_POST_BY_ID = "SELECT " + "*" + " from " + POST_TABLE + " where " + ID + " = ?";
    String QUERY_ALL_POST_BY_LOCATION = "SELECT " + "*" + " from " + POST_TABLE + " where " + POST_LOCATION + " = ?";
    String QUERY_ALL_COMMENTS_BY_POST_ID = "SELECT " + "*" + " from " + COMMENT_TABLE + " where " + ID_POST + " = ?";
    String QUERY_ALL_COMMENTS_BY_POST_ID_NEXT_PAGE = "SELECT * FROM " + COMMENT_TABLE + " where " + ID_POST + " = ? " + " AND " + COMMENT_DATE + " < ?";
    String QUERY_ID_USER_BY_NAME = "SELECT " + "ID" + " from " + USER_TABLE + " where " + USER_NAME + " = ?";
    String QUERY_GRADE_POST_BY_ID_POST = "SELECT " + "*" + " from " + GRADE_POST_TABLE + " where " + ID_POST + " = ?";
    String QUERY_GRADE_POST_BY_ID_POST_AND_USER = "SELECT * FROM " + GRADE_POST_TABLE + " WHERE " + ID_POST + " = ? AND " + USER_NAME + "  = ?";
    String QUERY_GRADE_COMMENT_BY_ID_COMMENT = "SELECT " + "*" + " from " + GRADE_COMMENT_TABLE + " where " + ID_COMMENT + " = ?";
    String QUERY_GRADE_COMMENT_BY_ID_COMMENT_AND_USER = "SELECT * FROM " + GRADE_COMMENT_TABLE + " where " + ID_COMMENT + " = ? AND " + USER_NAME + "  = ?";
    String QUERY_PHOTO_BY_ID_POST = "SELECT " + "*" + " from " + PHOTO_TABLE + " where " + ID_POST + " = ?";
}
