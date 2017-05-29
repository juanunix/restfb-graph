package com.fbcrawler;

import com.restfb.*;
import com.restfb.types.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Crawler for crawling comments from fb-page
 *
 * @author delsner
 */
public class FbCrawlerCommentsRunner {

    /**
     * Constants for setting pages and time period
     */
    private static final String[] COMMENTS = new String[]{
            "215982125159841_1397044187053623",
    };

    /**
     * Static initializer for logging
     */
    static {
        try {
            LogManager.getLogManager().readConfiguration(FbCrawlerRunner.class.getResourceAsStream("/logging.properties"));
        } catch (Exception e) {
            throw new IllegalStateException("Could not read in logging configuration", e);
        }
    }

    /**
     * Entry point. You must provide a one argument on the command line: a valid Graph API access token
     *
     * @param args Command-line arguments.
     * @throws IllegalArgumentException If no command-line arguments are provided.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "You must provide an OAuth access token parameter. " + "See README for more information.");
        }

        FacebookClient facebookClient = new DefaultFacebookClient(args[0], Version.LATEST);

        for (String comment : COMMENTS) {
            // Get connection of posts-pages for time period for page
            Connection<Comment> commentsPages = facebookClient.fetchConnection(
                    comment + "/comments",
                    Comment.class,
                    Parameter.with("fields", "comments.limit(10000).summary(true){comments.limit(10000).summary(true){comments.limit(10000).summary(true){message,like_count,parent,from,id,created_time},message,like_count,parent,from,id,created_time},message,like_count,parent,from,id,created_time},message,from,id,created_time"),
                    Parameter.with("limit", 10000));
            List<Comment> comments = new ArrayList<Comment>();
            // For each comments-page get comments
            for (List<Comment> commentPage : commentsPages) {
                for (Comment c : commentPage) {
                    comments.add(c);
                    for(Comment c1: c.getComments().getData()) {
                        comments.add(c1);
                        for(Comment c2: c1.getComments().getData()) {
                            comments.add(c2);
                            for(Comment c3: c2.getComments().getData()) {
                                comments.add(c3);
                            }
                        }
                    }
                }
            }

            // write all comments to csv
            CsvFileWriter.writeComments("comments_for_post_" + comment + ".csv", comments);
        }
    }
}
