package ua.org.javatraining.automessenger.app.services;

import android.content.Context;
import android.util.Log;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entities.*;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.CommentGrades;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.PostGrades;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource implements DataSource {

    private CommentService commentService;
    private PostService postService;
    private PhotoService photoService;
    private SubscriptionService subscriptionService;
    private UserService userService;
    private GradeCommentService gradeCommentService;
    private GradePostService gradePostService;
    private TagService tagService;
    private Context context;

    public LocalDataSource(Context context) {
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(context);
        commentService = new CommentService(sqLiteAdapter);
        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        userService = new UserService(sqLiteAdapter);
        gradeCommentService = new GradeCommentService(sqLiteAdapter);
        gradePostService = new GradePostService(sqLiteAdapter);
        tagService = new TagService(sqLiteAdapter);
        this.context = context;
    }

    @Override
    public void setUser(String username) {
        if (userService.getUser(username) == null) {
            User user = new User();
            user.setName(username);
            userService.insertUser(user);
        }
    }

    @Override
    public FullPost getPostByID(long postID) {
        Post post = postService.getPostByID(postID);
        Photo photo = photoService.getPhoto((int) postID);
        FullPost fullPost = null;

        if (post != null && photo != null) {
            fullPost = new FullPost(post);
            fullPost.setCommentCount(commentService.getCommentCountByPostID((int) post.getId()));
            fullPost.getPhotos().add(photo.getPhotoLink());
        }

        return fullPost;
    }

    @Override
    public List<FullPost> getPostsByAuthor() {
        List<Post> posts = postService.getPostsByAuthor(Authentication.getLastUser(context));
        List<FullPost> fPosts = null;

        if (posts != null) {
            fPosts = new ArrayList<FullPost>();

            for (Post p : posts) {
                FullPost fPost = new FullPost(p);
                Log.i("mytag", "post id: " + p.getId());
                Photo photo = photoService.getPhoto((int) p.getId());
                Log.i("mytag", (photo == null) ? "photo = null" : photo.getPhotoLink());
                fPost.setCommentCount(commentService.getCommentCountByPostID((int) p.getId()));

                if (photo != null) {
                    fPost.getPhotos().add(photo.getPhotoLink());
                }

                fPosts.add(fPost);
            }
        }

        return fPosts;
    }

    @Override
    public List<FullPost> getPostsFromSubscriptions() {
        List<Post> posts = postService.getPostsFromSubscribes(Authentication.getLastUser(context));
        List<FullPost> fPosts = null;

        if (posts != null) {
            fPosts = new ArrayList<FullPost>();

            for (Post p : posts) {
                FullPost fPost = new FullPost(p);
                Photo photo = photoService.getPhoto((int) p.getId());
                fPost.setCommentCount(commentService.getCommentCountByPostID((int) p.getId()));

                if (photo != null) {
                    fPost.getPhotos().add(photo.getPhotoLink());
                }

                fPosts.add(fPost);
            }
        }

        return fPosts;
    }

    @Override
    public List<FullPost> getPostsFromSubscriptions(long timestamp) {
        List<Post> posts = postService.getPostsFromSubscribesNextPage(Authentication.getLastUser(context), timestamp);
        List<FullPost> fPosts = null;

        if (posts != null) {
            fPosts = new ArrayList<FullPost>();

            for (Post p : posts) {
                FullPost fPost = new FullPost(p);
                Photo photo = photoService.getPhoto((int) p.getId());
                fPost.setCommentCount(commentService.getCommentCountByPostID((int) p.getId()));

                if (photo != null) {
                    fPost.getPhotos().add(photo.getPhotoLink());
                }

                fPosts.add(fPost);
            }
        }

        return fPosts;
    }

    @Override
    public List<FullPost> getPostsByLocation(ShortLocation shortLocation) {

        return null;
    }

    @Override
    public List<FullPost> getPostsByLocation(ShortLocation shortLocation, long timestamp) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByTagName(String tagName) {
        List<Post> posts = postService.getPostsByTag(tagName);
        List<FullPost> fPosts = null;

        if (posts != null) {
            fPosts = new ArrayList<FullPost>();

            for (Post p : posts) {
                FullPost fPost = new FullPost(p);
                Photo photo = photoService.getPhoto((int) p.getId());
                fPost.setCommentCount(commentService.getCommentCountByPostID((int) p.getId()));

                if (photo != null) {
                    fPost.getPhotos().add(photo.getPhotoLink());
                }

                fPosts.add(fPost);
            }
        }

        return fPosts;
    }

    @Override
    public List<FullPost> getPostsByTagName(String tagName, long timestamp) {
        List<Post> posts = postService.getPostsByTagNextPage(tagName, timestamp);
        List<FullPost> fPosts = null;

        if (posts != null) {
            fPosts = new ArrayList<FullPost>();

            for (Post p : posts) {
                FullPost fPost = new FullPost(p);
                Photo photo = photoService.getPhoto((int) p.getId());
                fPost.setCommentCount(commentService.getCommentCountByPostID((int) p.getId()));

                if (photo != null) {
                    fPost.getPhotos().add(photo.getPhotoLink());
                }

                fPosts.add(fPost);
            }
        }

        return fPosts;
    }

    @Override
    public long addPost(FullPost fullPost) {
        Post post = new Post();
        Photo photo = new Photo();
        fullPost.setAuthor(Authentication.getLastUser(context));

        fullPost.separate(post, photo);
        if (tagService.getTag(post.getNameTag()) == null) {
            Tag tag = new Tag();
            tag.setTagName(post.getNameTag());
            tagService.insertTag(tag);
        }

        long postID = postService.insertPost(post).getId();
        photo.setIdPost((int) postID);
        photoService.insertPhoto(photo);

        return postID;
    }

    @Override
    public List<Comment> getComments(long postID) {
        return commentService.getAllComments((int) postID);
    }

    @Override
    public List<Comment> getComments(long postID, long timestamp) {
        return commentService.getAllCommentsNextPage((int) postID, timestamp);
    }

    @Override
    public long addComment(Comment comment) {
        if (comment != null) {
            comment.setNameUser(Authentication.getLastUser(context));
            return commentService.insertComment(comment).getId();
        }
        return 0;
    }

    @Override
    public List<Subscription> getSubscriptions() {
        User user = new User();
        user.setName(Authentication.getLastUser(context));

        return subscriptionService.getSubscriptionsList(user);
    }

    @Override
    public Subscription addSubscription(String tag) {
        Subscription subscription = new Subscription();
        subscription.setNameUser(Authentication.getLastUser(context));
        subscription.setNameTag(tag);

        return subscriptionService.insertSubscription(subscription);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        if (subscription != null)
            subscriptionService.deleteSubscription(subscription);

    }

    @Override
    public PostGrades getPostGrades(long postID) {
        String userName = Authentication.getLastUser(context);
        List<GradePost> gPosts = gradePostService.getPostGrades(postID);

        PostGrades grades = new PostGrades();
        grades.setPostID(postID);
        grades.setUserName(userName);
        grades.setUserGrade(0);
        int grade = 0;

        if (gPosts != null) {
            for (GradePost gp : gPosts) {
                if (gp.getNameUser().equals(userName)) {
                    grades.setUserGrade(gp.getGrade());
                } else {
                    grade += gp.getGrade();
                }
            }
        }

        grades.setSumGrade(grade);

        return grades;
    }

    @Override
    public GradePost getCurrentUserPostGrade(long postID) {
        return gradePostService.getPostGrade(postID, Authentication.getLastUser(context));
    }

    @Override
    public void setCurrentUserPostGrade(long postID, int grade) {
        GradePost oldGradePost = getCurrentUserPostGrade(postID);

        if (grade < -1)
            grade = -1;
        else if (grade > 1)
            grade = 1;

        GradePost newGradePost = new GradePost();
        newGradePost.setNameUser(Authentication.getLastUser(context));
        newGradePost.setGrade(grade);
        newGradePost.setIdPost((int) postID);

        if (oldGradePost == null) {
            gradePostService.insertGradePost(newGradePost);
        } else {
            gradePostService.updateGradePost(newGradePost);
        }
    }

    @Override
    public CommentGrades getCommentGrades(long commentID) {
        String userName = Authentication.getLastUser(context);
        ArrayList<GradeComment> gCoomments = gradeCommentService.getCommentGrades(commentID);
        CommentGrades grades = new CommentGrades();
        grades.setCommentID(commentID);
        grades.setUserName(userName);
        int grade = 0;

        for (GradeComment gc : gCoomments) {
            if (gc.getNameUser().equals(userName)) {
                grades.setUserGrade(gc.getGrade());
            } else {
                grade += gc.getGrade();
            }
        }

        grades.setSumGrade(grade);

        return grades;
    }

    @Override
    public GradeComment getCurrentUserCommentGrade(long commentID) {
        return gradeCommentService.getCommentGrade(commentID, Authentication.getLastUser(context));
    }

    @Override
    public void setCurrentUserCommentGrade(long commentID, int grade) {
        GradeComment oldGradeComment = getCurrentUserCommentGrade(commentID);

        if (grade < -1)
            grade = -1;
        else if (grade > 1)
            grade = 1;

        GradeComment newGradeComment = new GradeComment();
        newGradeComment.setNameUser(Authentication.getLastUser(context));
        newGradeComment.setGrade(grade);
        newGradeComment.setIdComment((int) commentID);

        if (oldGradeComment == null) {
            gradeCommentService.insertGradeComment(newGradeComment);
        } else {
            gradeCommentService.updateGradeComment(newGradeComment);
        }
    }

    @Override
    public List<Tag> findSomeTags(String request) {
        return tagService.searchTags(request);
    }
}

