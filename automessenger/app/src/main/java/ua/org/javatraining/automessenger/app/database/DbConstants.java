package ua.org.javatraining.automessenger.app.database;

/**
 * Created by berkut on 29.05.15.
 */
public interface DbConstants {

    public static final String MYDATABASE_NAME = "autoMessenger";
    public static final int MYDATABASE_VERSION = 1;
    //Запросы создания таблиц
    public static final String USER_CREATE = "create table IF NOT EXISTS USER (ID integer primary key autoincrement not null unique, USER_NAME text);";
    public static final String TAG_CREATE = "create table IF NOT EXISTS TAG (ID integer primary key not null, TAG_NAME text);";
    public static final String SUBSCRIPTION_CREATE = "create table IF NOT EXISTS SUBSCRIPTION (ID integer primary key not null unique, ID_USER integer, ID_TAG integer, CONSTRAINT fk_subscription_user_parent FOREIGN KEY (id_user) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT fk_subscription_tag_parent FOREIGN KEY (id_tag) REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String POST_CREATE = "create table IF NOT EXISTS POST (ID integer primary key autoincrement not null unique, POST_TEXT text, POST_DATE integer, POST_LOCATION text, ID_USER integer, ID_TAG integer, CONSTRAINT fk_post_user_parent\n" +
            "\tFOREIGN KEY (id_user)\n" +
            "\tREFERENCES user (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE,\n" +
            " CONSTRAINT fk_post_tag_parent\n" +
            "\tFOREIGN KEY (id_tag)\n" +
            "\tREFERENCES tag (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE);";
    public static final String COMMENT_CREATE = "create table IF NOT EXISTS COMMENT (ID integer primary key autoincrement not null unique, COMMENT_DATE integer not null, COMMENT_TEXT text, ID_USER integer, ID_POST integer, CONSTRAINT fk_comment_user_parent\n" +
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
    public static final String GRADE_POST_CREATE = "create table IF NOT EXISTS GRADE_POST (ID integer primary key not null, ID_USER integer, ID_POST integer, GRADE integer, CONSTRAINT fk_grade_post_user_parent\n" +
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
    public static final String GRADE_COMMENT_CREATE = "create table IF NOT EXISTS GRADE_COMMENT (ID integer primary key autoincrement not null unique, ID_USER integer, ID_COMMENT integer, GRADE integer,  CONSTRAINT fk_grade_comment_user_parent\n" +
            "    FOREIGN KEY (id_user)\n" +
            "    REFERENCES user (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE,\n" +
            "    CONSTRAINT fk_grade_comment_comment_parent\n" +
            "    FOREIGN KEY (id_comment)\n" +
            "    REFERENCES comment (id)\n" +
            "    ON DELETE CASCADE\n" +
            "    ON UPDATE CASCADE);";
    public static final String PHOTO_CREATE = "create table IF NOT EXISTS PHOTO (ID integer primary key autoincrement not null unique, LINK text, ID_POST integer,  CONSTRAINT fk_photo_post_parent\n" +
            "\tFOREIGN KEY (id_post)\n" +
            "\tREFERENCES post (id)\n" +
            "\t\t ON DELETE CASCADE\n" +
            "\t  ON UPDATE CASCADE)";
    //Константы для таблиц
    public static final String ID = "ID";
    public static final String USER_TABLE = "USER";
    public static final String USER_NAME = "USER_NAME";

    public static final String TAG_TABLE = "TAG";
    public static final String TAG_NAME = "TAG_NAME";

    public static final String SUBSCRIPTION_TABLE = "SUBSCRIPTION";
    public static final String USER_ID = "ID_USER";
    public static final String TAG_ID = "ID_TAG";

    public static final String POST_TABLE = "POST";
    public static final String POST_TEXT = "POST_TEXT";
    public static final String POST_DATE = "POST_DATE";
    public static final String POST_LOCATION = "POST_LOCATION";

    public static final String COMMENT_TABLE = "COMMENT";
    public static final String COMMENT_DATE = "COMMENT_DATE";
    public static final String COMMENT_TEXT = "COMMENT_TEXT";
    public static final String ID_POST = "ID_POST";

    public static final String GRADE_POST_TABLE = "GRADE_POST";
    public static final String GRADE = "GRADE";

    public static final String GRADE_COMMENT_TABLE = "GRADE_COMMENT";
    public static final String ID_COMMENT = "ID_COMMENT";

    public static final String LINK = "LINK";
}
