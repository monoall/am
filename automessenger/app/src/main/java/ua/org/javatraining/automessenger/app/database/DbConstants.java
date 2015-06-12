package ua.org.javatraining.automessenger.app.database;

public interface DbConstants {

    String MYDATABASE_NAME = "autoMessenger";
    int MYDATABASE_VERSION = 1;
    //Запросы создания таблиц
    String USER_CREATE = "create table IF NOT EXISTS USER (ID integer primary key autoincrement not null unique, USER_NAME text);";
    String TAG_CREATE = "create table IF NOT EXISTS TAG (ID integer primary key not null, TAG_NAME text);";
    String SUBSCRIPTION_CREATE = "create table IF NOT EXISTS SUBSCRIPTION (ID integer primary key not null unique, ID_USER integer, ID_TAG integer, CONSTRAINT fk_subscription_user_parent FOREIGN KEY (id_user) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT fk_subscription_tag_parent FOREIGN KEY (id_tag) REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE);";
    String POST_CREATE = "create table IF NOT EXISTS POST (ID integer primary key autoincrement not null unique, POST_TEXT text, POST_DATE integer, POST_LOCATION text, ID_USER integer, ID_TAG integer, CONSTRAINT fk_post_user_parent\n" +
            "\tFOREIGN KEY (id_user)\n" +
            "\tREFERENCES user (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE,\n" +
            " CONSTRAINT fk_post_tag_parent\n" +
            "\tFOREIGN KEY (id_tag)\n" +
            "\tREFERENCES tag (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE);";
    String COMMENT_CREATE = "create table IF NOT EXISTS COMMENT (ID integer primary key autoincrement not null unique, COMMENT_DATE integer not null, COMMENT_TEXT text, ID_USER integer, ID_POST integer, CONSTRAINT fk_comment_user_parent\n" +
            "    FOREIGN KEY (id_user)\n" +
            "    REFERENCES user (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "\n" +
            "    CONSTRAINT fk_comment_post_parent\n" +
            "    FOREIGN KEY (id_post)\n" +
            "    REFERENCES `post`(id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String GRADE_POST_CREATE = "create table IF NOT EXISTS GRADE_POST (ID integer primary key not null, ID_USER integer, ID_POST integer, GRADE integer, CONSTRAINT fk_grade_post_user_parent\n" +
            "    FOREIGN KEY (id_user)\n" +
            "    REFERENCES user (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "\n" +
            "    CONSTRAINT fk_grade_post_comment_parent\n" +
            "    FOREIGN KEY (id_post)\n" +
            "    REFERENCES comment (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String GRADE_COMMENT_CREATE = "create table IF NOT EXISTS GRADE_COMMENT (ID integer primary key autoincrement not null unique, ID_USER integer, ID_COMMENT integer, GRADE integer,  CONSTRAINT fk_grade_comment_user_parent\n" +
            "    FOREIGN KEY (id_user)\n" +
            "    REFERENCES user (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "    CONSTRAINT fk_grade_comment_comment_parent\n" +
            "    FOREIGN KEY (id_comment)\n" +
            "    REFERENCES comment (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    String PHOTO_CREATE = "create table IF NOT EXISTS PHOTO (ID integer primary key autoincrement not null unique, LINK text, ID_POST integer,  CONSTRAINT fk_photo_post_parent\n" +
            "\tFOREIGN KEY (id_post)\n" +
            "\tREFERENCES post (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE)";
    //Константы для таблиц
    String ID = "ID";
    String USER_TABLE = "USER";
    String USER_NAME = "USER_NAME";

    String TAG_TABLE = "TAG";
    String TAG_NAME = "TAG_NAME";

    String SUBSCRIPTION_TABLE = "SUBSCRIPTION";
    String USER_ID = "ID_USER";
    String TAG_ID = "ID_TAG";

    String POST_TABLE = "POST";
    String POST_TEXT = "POST_TEXT";
    String POST_DATE = "POST_DATE";
    String POST_LOCATION = "POST_LOCATION";

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
    String QUERY_USER_BY_ID = "SELECT " + USER_NAME + " from " + USER_TABLE + " where " + ID + " = ?";
    String QUERY_TAG_BY_ID = "SELECT " + TAG_NAME + " from " + TAG_TABLE + " where " + ID + " = ?";
    String QUERY_ALL_TAG_ID_BY_USER_ID = "SELECT " + "*" + " from " + SUBSCRIPTION_TABLE + " where " + USER_ID + " = ?";
    String QUERY_ALL_POST_BY_USER_ID_AND_TAG_ID = "SELECT " + "*" + " from " + POST_TABLE + " where " + USER_ID + " = ?" + " and " + TAG_ID + " = ?";
    String QUERY_ALL_POST_BY_TAG_ID = "SELECT " + "*" + " from " + POST_TABLE + " where " + TAG_ID + " = ?";
    String QUERY_ALL_POST_BY_USER_ID = "SELECT " + "*" + " from " + POST_TABLE + " where " + USER_ID + " = ?";
    String QUERY_ALL_POST_BY_LOCATION = "SELECT " + "*" + " from " + POST_TABLE + " where " + POST_LOCATION + " = ?";
    String QUERY_ALL_COMMENTS_BY_POST_ID = "SELECT " + "*" + " from " + COMMENT_TABLE + " where " + ID_POST + " = ?";
    String QUERY_ID_USER_BY_NAME = "SELECT " + "ID" + " from " + USER_TABLE + " where " + USER_NAME + " = ?";
    String QUERY_GRADE_POST_BY_ID_POST = "SELECT " + "*" + " from " + GRADE_POST_TABLE + " where " + ID_POST + " = ?";
    String QUERY_GRADE_COMMENT_BY_ID_COMMENT = "SELECT " + "*" + " from " + GRADE_COMMENT_TABLE + " where " + ID_COMMENT + " = ?";
    String QUERY_PHOTO_BY_ID_POST = "SELECT " + "*" + " from " + PHOTO_TABLE + " where " + ID_POST + " = ?";

}
