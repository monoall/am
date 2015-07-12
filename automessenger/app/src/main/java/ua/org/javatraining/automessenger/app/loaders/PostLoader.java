package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class PostLoader extends AsyncTaskLoader<List<FullPost>> {

    String username;

    SQLiteAdapter sqLiteAdapter;
    PostService postService;
    PhotoService photoService;
    CommentService commentService;

    public PostLoader(Context context, String username) {
        super(context);
        this.username = username;
        Log.i("myTag", "PostLoader constructor");
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
    }

    @Override
    public List<FullPost> loadInBackground() {
        return updateData();
    }

    private List<FullPost> updateData(){
        Log.i("myTag", "updateData " + username);
        List<Post> posts = postService.getPostsFromSubscribes(username);
        List<FullPost> data = new ArrayList<FullPost>();
        if (posts != null) {
            for (Post p : posts) {
                FullPost fp = new FullPost(p);
                fp.getPhotos().add(photoService.getPhoto((int) p.getId()).getPhotoLink());//todo remove cast to int after DB fix
                fp.setCommentCount(commentService.getAllComments((int) p.getId()).size());
                data.add(fp);
            }
        }
        Log.i("myTag", "data from loader: " + Integer.toString(data.size()));
        return data;
    }
}
