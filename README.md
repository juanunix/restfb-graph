# restfb-graph
This project demonstrates the usage of the restfb library to gather data from the facebook graph api.

## setup
1. Get access token from https://developers.facebook.com/tools/explorer/
2. run `mvn exec:java@get-posts-from-page -Daccess_token=ACCESS_TOKEN`

## customize
In order to customize the crawler go to `FbCrawlerRunner.java` and adjust `PAGE_NAME`, `START_DATE` or some of the parameters for fetching the posts.
The documentation for restfb can be found at http://restfb.com/documentation/.
