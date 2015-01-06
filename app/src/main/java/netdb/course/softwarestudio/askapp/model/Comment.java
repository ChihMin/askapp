package netdb.course.softwarestudio.askapp.model;

import android.widget.EditText;

import netdb.course.softwarestudio.askapp.MainActivity;
import netdb.course.softwarestudio.askapp.service.rest.model.Resource;

public class Comment extends Resource{

    private String title;
    private String content;

    public static String getCollectionName() {
        EditText tmp = MainActivity.keywordEdt;
        String title = tmp.getText().toString();
        return "comments/" + title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
