package com.fbcrawler;

import com.restfb.types.Comment;
import com.restfb.types.Post;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Class to write data into csv files
 *
 * @author delsner
 */
public class CsvFileWriter {

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String QUOTES = "\"";

    // CSV file headers
    private static final String POST_FILE_HEADER = "id,name,message,story,link,description,created_time,likes,shares,comments";
    private static final String COMMENT_FILE_HEADER = "id,message";

    public static void writePosts(String fileName, List<Post> posts) {

        OutputStreamWriter fileWriter = null;

        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8").newEncoder());

            // Write the CSV file header and add new line
            fileWriter.append(POST_FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write posts to csv file
            for (Post post : posts) {
                fileWriter.append(post.getId());
                fileWriter.append(COMMA_DELIMITER);
                String name = post.getName() != null ? formatText(post.getName()) : "";
                fileWriter.append(name);
                fileWriter.append(COMMA_DELIMITER);
                String message = post.getMessage() != null ? formatText(post.getMessage()) : "";
                fileWriter.append(message);
                fileWriter.append(COMMA_DELIMITER);
                String story = post.getStory() != null ? formatText(post.getStory()) : "";
                fileWriter.append(story);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(post.getLink());
                fileWriter.append(COMMA_DELIMITER);
                String desc = post.getDescription() != null ? formatText(post.getDescription()) : "";
                fileWriter.append(desc);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(post.getCreatedTime().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(post.getLikesCount().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(post.getSharesCount().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(post.getCommentsCount().toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeComments(String fileName, List<Comment> comments) {

        OutputStreamWriter fileWriter = null;

        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8").newEncoder());

            // Write the CSV file header and add new line
            fileWriter.append(POST_FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write posts to csv file
            for (Comment comment : comments) {
                fileWriter.append(String.valueOf(comment.getId()));
                fileWriter.append(COMMA_DELIMITER);
                String message = comment.getMessage() != null ? formatText(comment.getMessage()) : "";
                fileWriter.append(message);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatText(String text) {
        return QUOTES + text.replace("\r\n", " ")
                        .replace("\n", " ")
                        .replace("\\", "")
                        .replace("\"", "\'\'")
                        .replace(";", ":") + QUOTES;
    }
}
