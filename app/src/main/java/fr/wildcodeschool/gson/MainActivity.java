package fr.wildcodeschool.gson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.InputStream;

import fr.wildcodeschool.gson.parser.Gson;

public class MainActivity extends AppCompatActivity {
  private static final String MSG_OK = "GSON PARSER IS CORRECT";
  private static final String MSG_KO = "GSON PARSER HAS FAILED";
  private static final String MSG_NULL = "GSON PARSER HAS ENCOUNTERED UNEXPECTED BEHAVIOR";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    InputStream in = getResources().openRawResource(R.raw.data);
    Database db = new Gson().fromJson(in, Database.class);

    // Read the parsed content and display the result msg
    String msg = (null == db) ? MSG_NULL : db.checkData() ? MSG_OK : MSG_KO;
    ((TextView)findViewById(R.id.textView)).setText(msg);
  }
}
