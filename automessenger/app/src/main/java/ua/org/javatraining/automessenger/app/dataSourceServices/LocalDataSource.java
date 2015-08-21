package ua.org.javatraining.automessenger.app.dataSourceServices;

import android.content.Context;
import android.util.Log;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entities.*;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.*;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource implements DataSource {

    private UploadQueueService queueService;
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
        queueService = new UploadQueueService(sqLiteAdapter);
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
        if (!username.equals("") && userService.getUser(username) == null) {
            User user = new User();
            user.setName(username);
            userService.insertUser(user);
            queueService.insertInQueue(user);
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
        List<FullPost> fPosts = new ArrayList<FullPost>();

        if (posts != null) {
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
        List<FullPost> fPosts = new ArrayList<FullPost>();

        if (shortLocation != null) {
            List<Post> posts;

            if (shortLocation.getCountry().equals("")) {   //if ShortLocation class don't have even country return empty List
                return fPosts;
            } else if (shortLocation.getAdminArea().equals("")) { //if ShortLocation contain country but don't contain AdminArea we search only by country
                posts = postService.getPostsByLocationWithOneWord(shortLocation.getCountry());
            } else if (shortLocation.getRegion().equals("")) { //if ShortLocation contain country and AdminArea but don't contain Region we search only by country and AdminArea
                posts = postService.getPostsByLocationWithTwoWords(shortLocation.getCountry(), shortLocation.getAdminArea());
            } else { //ShortLocation have all fields, make full search
                posts = postService.getPostsByLocationWithThreeWords(shortLocation.getCountry(), shortLocation.getAdminArea(), shortLocation.getRegion());
            }

            if (posts != null) {
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

        return fPosts;
    }

    @Override
    public List<FullPost> getPostsByLocation(ShortLocation shortLocation, long timestamp) {
        List<FullPost> fPosts = new ArrayList<FullPost>();

        if (shortLocation != null) {
            List<Post> posts;

            if (shortLocation.getCountry().equals("")) {   //if ShortLocation class don't have even country return empty List
                return fPosts;
            } else if (shortLocation.getAdminArea().equals("")) { //if ShortLocation contain country but don't contain AdminArea we search only by country
                posts = postService.getPostsByLocationWithOneWordNextPage(shortLocation.getCountry(), timestamp);
            } else if (shortLocation.getRegion().equals("")) { //if ShortLocation contain country and AdminArea but don't contain Region we search only by country and AdminArea
                posts = postService.getPostsByLocationWithTwoWordsNextPage(shortLocation.getCountry(), shortLocation.getAdminArea(), timestamp);
            } else { //ShortLocation have all fields, make full search
                posts = postService.getPostsByLocationWithThreeWordsNextPage(shortLocation.getCountry(), shortLocation.getAdminArea(), shortLocation.getRegion(), timestamp);
            }

            if (posts != null) {
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

        return fPosts;
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
        List<FullPost> fPosts = new ArrayList<FullPost>();

        if (posts != null) {

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
        if (tagService.getTag(post.getTagName()) == null) {
            Tag tag = new Tag();
            tag.setTagName(post.getTagName());
            tagService.insertTag(tag);
        }

        long postID = postService.insertPost(post).getId();
        photo.setPostId((int) postID);
        photoService.insertPhoto(photo);

        if (postID != 0){
            queueService.insertInQueue(post);
        }

            return postID;
    }

    @Override
    public List<SuperComment> getComments(long postID) {
        List<Comment> comments = commentService.getAllComments((int) postID);
        ArrayList<SuperComment> superComments = new ArrayList<SuperComment>();

        for (Comment c : comments) {
            SuperComment sc = new SuperComment(c);
            int grade = 0;
            sc.setUserGrade(0);
            String username = Authentication.getLastUser(context);
            ArrayList<GradeComment> cGrades = gradeCommentService.getCommentGrades(c.getId());

            for (GradeComment gc : cGrades) {
                if (gc.getUserId().equals(username)) {
                    sc.setUserGrade(gc.getGrade());
                } else {
                    grade += gc.getGrade();
                }
            }

            sc.setGradeNumber(grade);
            superComments.add(sc);
        }

        return superComments;
    }

    @Override
    public List<SuperComment> getComments(long postID, long timestamp) {
        List<Comment> comments = commentService.getAllCommentsNextPage((int) postID, timestamp);
        ArrayList<SuperComment> superComments = new ArrayList<SuperComment>();

        for (Comment c : comments) {
            SuperComment sc = new SuperComment(c);
            int grade = 0;
            sc.setUserGrade(0);
            String username = Authentication.getLastUser(context);
            ArrayList<GradeComment> cGrades = gradeCommentService.getCommentGrades(c.getId());

            for (GradeComment gc : cGrades) {
                if (gc.getUserId().equals(username)) {
                    sc.setUserGrade(gc.getGrade());
                } else {
                    grade += gc.getGrade();
                }
            }

            sc.setGradeNumber(grade);
            superComments.add(sc);
        }

        return superComments;
    }

    @Override
    public long addComment(Comment comment) {
        if (comment != null) {
            comment.setUserId(Authentication.getLastUser(context));
            queueService.insertInQueue(comment);
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
        subscription.setUserId(Authentication.getLastUser(context));
        subscription.setTagId(tag);
        queueService.insertInQueue(subscription);
        return subscriptionService.insertSubscription(subscription);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        if (subscription != null) {
            subscriptionService.deleteSubscription(subscription);
            queueService.insertInQueueForDelete(subscription);
        }
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
                if (gp.getUserId().equals(userName)) {
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
        newGradePost.setUserId(Authentication.getLastUser(context));
        newGradePost.setGrade(grade);
        newGradePost.setIdPost((int) postID);

        if (oldGradePost == null) {
            gradePostService.insertGradePost(newGradePost);
        } else {
            gradePostService.updateGradePost(newGradePost);
        }

        queueService.insertInQueue(newGradePost);
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
            if (gc.getUserId().equals(userName)) {
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
        newGradeComment.setUserId(Authentication.getLastUser(context));
        newGradeComment.setGrade(grade);
        newGradeComment.setCommentId((int) commentID);

        if (oldGradeComment == null) {
            gradeCommentService.insertGradeComment(newGradeComment);
        } else {
            gradeCommentService.updateGradeComment(newGradeComment);
        }

        queueService.insertInQueue(newGradeComment);
    }

    @Override
    public List<Tag> findSomeTags(String request) {
        return tagService.searchTags(request);
    }
}

