package ua.org.javatraining.automessenger.app.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Comment;

import java.util.List;

public class CommentLoader extends AsyncTaskLoader<List<Comment>> {

    long postID;
    SQLiteAdapter sqLiteAdapter;
    CommentService commentService;

    public CommentLoader(Context context, long postID) {
        super(context);
        this.postID = postID;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        commentService = new CommentService(sqLiteAdapter);
    }

    @Override
    public List<Comment> loadInBackground() {
        return commentService.getAllComments((int) postID);
    }
}
