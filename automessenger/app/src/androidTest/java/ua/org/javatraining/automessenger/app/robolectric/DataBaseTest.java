package ua.org.javatraining.automessenger.app.robolectric;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by berkut on 04.06.15.
 */
@SuppressWarnings("DefaultFileTemplate")
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DataBaseTest {

    MainActivity mainActivity;
    SQLiteAdapter sqLiteAdapter;
    UserService userService;
    TagService tagService;
    SubscriptionService subscriptionService;
    PostService postService;
    CommentService commentService;

    @Before
    public void initialize(){
        mainActivity = new MainActivity();
        sqLiteAdapter = SQLiteAdapter.initInstance(mainActivity);
        userService = new UserService(sqLiteAdapter);
        tagService = new TagService(sqLiteAdapter);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        postService = new PostService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
    }

   /* @Test
    public void testInsertUser(){
        User user = new User();
        user.setName("Tom");
        User user2 = new User();
        user2.setName("Jack");
        User user3 = new User();
        user3.setName("Ron");
        userService.insertUser(user);
        userService.insertUser(user2);
        userService.insertUser(user3);
        User checkUser = userService.getUserById(2);
        assertEquals("Jack", checkUser.getName());
    }

    @Test
    public void testQueryIdFromUser(){
        User user = new User();
        user.setName("Tom");
        User user2 = new User();
        user2.setName("Jack");
        User user3 = new User();
        user3.setName("Ron");
        userService.insertUser(user);
        userService.insertUser(user2);
        userService.insertUser(user3);
        User checkUser = userService.queryIdFromUser("Ron");
        assertEquals(3, checkUser.getId());
    }*/

    /*
    //Not compile
    @Test
    public void testDeleteUser(){
        User user = new User();
        user.setName("Tom");
        User user2 = new User();
        user2.setName("Jack");
        userService.insertUser(user);
        userService.insertUser(user2);
        User checkUser = userService.getUserById(1);
        userService.deleteUser(checkUser);
        User checkUser2 = userService.getUserById(1);
        assertEquals("Jack", checkUser2.getName());
    }*/


    //Tags

    /*@Test
    public void testInsertTag(){
        Tag tag = new Tag();
        Tag tag2 = new Tag();
        Tag tag3 = new Tag();
        tag.setTagName("BE 0208");
        tag2.setTagName("BE 1228");
        tag3.setTagName("BE 0268");
        tagService.insertTag(tag);
        tagService.insertTag(tag2);
        tagService.insertTag(tag3);
        Tag checkTag = tagService.getTagById(2);
        assertEquals("BE 1228", checkTag.getTagName());
    }*/

   /*
   //Not compile
   @Test
   public void testDeleteTag(){
        Tag tag = new Tag();
        Tag tag2 = new Tag();
        Tag tag3 = new Tag();
        tag.setTagName("BE 0208");
        tag2.setTagName("BE 1228");
        tag3.setTagName("BE 0268");
        tagService.insertTag(tag);
        tagService.insertTag(tag2);
        tagService.insertTag(tag3);
        Tag checkTag = tagService.getTagById(1);
        tagService.deleteTag(checkTag);
        Tag checkTag2 = tagService.getTagById(1);
        assertEquals("BE 1228", checkTag2.getTagName());
    }*/


    //Post


    @Test
    public void testInsertPost(){
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        post1.setPostText("Some Text1");
        post1.setPostLocation("location1");
        post1.setPostDate(120215);
        post1.setIdUser(1);
        post1.setIdTag(1);
        post2.setPostText("Some Text2");
        post2.setPostLocation("location2");
        post2.setPostDate(110215);
        post2.setIdUser(1);
        post2.setIdTag(1);
        post3.setPostText("Some Text3");
        post3.setPostLocation("location3");
        post3.setPostDate(110216);
        post3.setIdUser(2);
        post3.setIdTag(2);
        postService.insertPost(post1);
        postService.insertPost(post2);
        postService.insertPost(post3);
        ArrayList<Post> list = postService.getAllPosts(1,1);
        assertEquals(2 ,list.size());
    }

    @Test
    public void testGetPostById(){
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        post1.setPostText("Some Text1");
        post1.setPostLocation("location1");
        post1.setPostDate(120215);
        post1.setIdUser(1);
        post1.setIdTag(1);
        post2.setPostText("Some Text2");
        post2.setPostLocation("location2");
        post2.setPostDate(110215);
        post2.setIdUser(1);
        post2.setIdTag(1);
        post3.setPostText("Some Text3");
        post3.setPostLocation("location3");
        post3.setPostDate(110216);
        post3.setIdUser(2);
        post3.setIdTag(2);
        postService.insertPost(post1);
        postService.insertPost(post2);
        postService.insertPost(post3);
        Post checkPost = postService.getPostById(3);
        assertEquals("Some Text3", checkPost.getPostText());
    }

    @Test
    //Not compile
    public void deleteTest(){
        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();
        post1.setPostText("Some Text1");
        post1.setPostLocation("location1");
        post1.setPostDate(120215);
        post1.setIdUser(1);
        post1.setIdTag(1);
        post2.setPostText("SomeText 2");
        post2.setPostLocation("location2");
        post2.setPostDate(110215);
        post2.setIdUser(1);
        post2.setIdTag(1);
        post3.setPostText("Some Text3");
        post3.setPostLocation("location3");
        post3.setPostDate(110216);
        post3.setIdUser(2);
        post3.setIdTag(2);
        postService.insertPost(post1);
        postService.insertPost(post2);
        postService.insertPost(post3);
        Post checkPost = postService.getPostById(2);
        postService.deletePost(checkPost);
        Post checkPost2 = postService.getPostById(2);
        assertEquals("Some Text3", checkPost.getPostText());
    }

}
