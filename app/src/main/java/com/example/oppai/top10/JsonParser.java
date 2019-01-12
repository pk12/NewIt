package com.example.oppai.top10;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    private static final String TAG = "JsonParser";
    private static final String source_key = "source";
    private static final String source_id = "id";
    private static final String source_name = "name";

    private static final String Author_key = "author";
    private static final String Title_key = "title";
    private static final String description_key = "description";
    private static final String url_key = "url";
    private static final String image_url_key = "urlToImage";
    private static final String publish_date_key = "publishedAt";
    private static final String content_key = "content";

    private ArrayList<Article> articles;

    public JsonParser() {
        this.articles = new ArrayList<Article>();
    }

    public boolean parseJson(String jsonData){

        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("articles");

            for (int i = 0; i < array.length(); i++){

                JSONObject JsonArticle = array.getJSONObject(i);
                JSONObject source = JsonArticle.getJSONObject(source_key);

                String name = source.getString(source_name);
                String author = JsonArticle.getString(Author_key);
                String title = JsonArticle.getString(Title_key);
                String url = JsonArticle.getString(url_key);
                String imageUrl = JsonArticle.getString(image_url_key);
                String publishDate = JsonArticle.getString(publish_date_key);
                String content = JsonArticle.getString(content_key);
                String description = JsonArticle.getString(description_key);

                Article article = new Article(name,author,title,url,imageUrl,publishDate,content,description);
                articles.add(article);

            }
        }
        catch (JSONException e) {
            Log.e(TAG, "parseJson: Error parsing json data", e);
            return false;
        }

        return true;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
