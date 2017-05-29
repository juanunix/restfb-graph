package com.fbcrawler;

import com.restfb.*;
import com.restfb.types.Comment;
import com.restfb.types.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Crawler for crawling posts and comments from fb-page
 *
 * @author delsner
 */
public class FbCrawlerRunner {

    /**
     * Constants for setting pages and time period
     */
    private static final String[] PAGES = new String[]{
            "ihre.sz",
            "tagesschau",
            "taz.kommune",
            "spiegelonline",
            "zeitonline",
            "ZDFheute",
            "faz",
            "welt",
            "bild"
    };
    private static final String START_DATE = "31 August 2016";

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

        for (String page : PAGES) {
            // Get connection of posts-pages for time period for page
            Connection<Post> postsPages = facebookClient.fetchConnection(
                    page + "/posts",
                    Post.class,
                    Parameter.with("fields", "id,name,message,story,link,description,created_time,likes.limit(0).summary(true),comments.limit(0).summary(true),shares"), // TODO add comments (comments.limit(10000).summary(true){id,message}
                    Parameter.with("until", "now"),
                    Parameter.with("since", START_DATE),
                    Parameter.with("limit", 100));
            List<Post> posts = new ArrayList<Post>();
            // For each posts-page get posts
            for (List<Post> postPage : postsPages) {
                for (Post post : postPage) {
                    posts.add(post);
                    // CsvFileWriter.writeComments("comments_for_post_" + post.getId() + ".csv", post.getComments().getData()); // TODO write comments to file/db ?
                }
            }

            // write all posts to csv
            CsvFileWriter.writePosts(page + "_posts_" + new Date().getTime() + ".csv", posts);
        }
    }
}
