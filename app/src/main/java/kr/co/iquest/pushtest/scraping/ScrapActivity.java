package kr.co.iquest.pushtest.scraping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

import kr.co.iquest.pushtest.model.News;

public class ScrapActivity extends AppCompatActivity {

    private final String TAG = "ScrapActivity";
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kr.co.iquest.pushtest.R.layout.activity_scrap);

        textView = findViewById(kr.co.iquest.pushtest.R.id.textView);
        button = findViewById(kr.co.iquest.pushtest.R.id.btnView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new doSearch().execute();
            }
        });
    }

    public class doSearch extends AsyncTask<Void,Void,Void> {

        List<News> news = new ArrayList<News>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // 뉴스 검색 : https://search.daum.net/search?w=news&DA=STC&enc=utf8&cluster=y&cluster_page=1&sort=recency&p=1&q=검색어
                String uri = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid2=263&sid1=101&date=&page=2";
                //int page = 1;
                String searchText = "4호선";
                for (int page = 1; page < 2; page++) {
                    uri = "https://search.daum.net/search?w=news&DA=STC&enc=utf8&cluster=y&cluster_page=1&sort=recency&p=" + String.valueOf(page)
                     + "&q=" + searchText;

                    Document document = Jsoup.connect(uri).get();
                    //String html = document.html();
                    //news.addAll(parseSearchHtml(document));
                    parseSearchHtml(document);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String data = "";

            for (News newline : news) {
                data += newline.toString() + "\n";
            }

            Log.d(TAG, "data >> " + data);
            textView.setText(data);
        }

        private List<News> parseSearchHtml(Document doc) {
            List<News> news = new ArrayList<News>();

            String title = "";
            String time = "";
            Elements eles = doc.getElementsByClass("wrap_cont");
            for (Element ele: eles) {
                Elements tags = ele.getElementsByTag("a");
                for (Element tag: tags) {
                    title = tag.text();
                }
                Elements times = ele.getElementsByClass("cont_info");
                for (Element ti: times) {
                    time = ti.text();
                }

                Log.d(TAG, title + " - from : " + "fromx" + ", " + time);
            }
            return null;
        }
    }

    public class doIT extends AsyncTask<Void,Void,Void> {

        List<News> news = new ArrayList<News>();
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // 뉴스 검색 : https://search.daum.net/search?w=news&DA=STC&enc=utf8&cluster=y&cluster_page=1&sort=recency&p=1&q=검색어
                String uri= "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid2=263&sid1=101&date=&page=2";
                //int page = 1;
                for (int page = 1; page < 5; page++) {
                    uri = "https://news.daum.net/breakingnews/economic?page=" + String.valueOf(page);
                    Document document = Jsoup.connect(uri).get();
                    String html = document.html();
                    news.addAll(parseHtml(html));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String data = "";

            for(News newline : news) {
                data += newline.toString() + "\n";
            }

           Log.d(TAG, "data >> " + data);
            textView.setText(data);
        }



        private List<News>  parseHtml(String text) {

            List<News> news = new ArrayList<News>();

            String filter1 = "<strong class=\"tit_thumb\">";
            String filter2 = "<a href=\"";
            String filter2_1 = "\"";

            String filter3 = "class=\"link_txt\">";
            String filter3_1 = "</a>";

            String filter5 = "class=\"info_news\">";
            String filter5_1 = "<span class=\"txt_bar\"";

            String filter6 = "class=\"info_time\">";
            String filter6_1 = "</span>";

            while (text.indexOf(filter1) > 0) {
                text = text.substring(text.indexOf(filter1) + filter1.length());
                text = text.substring(text.indexOf(filter2) + filter2.length());

                //
                String clickUrl = text.substring(0, text.indexOf(filter2_1));

                text = text.substring(text.indexOf(filter3) + filter3.length());

                String title = text.substring(0, text.indexOf(filter3_1));

                // 출처, 시간이 없으면 pass~
                if (text.indexOf(filter5) < 0 || text.indexOf(filter6) < 0)
                    continue;

                text = text.substring(text.indexOf(filter5) + filter5.length());

                String from = text.substring(0, text.indexOf(filter5_1));

                text = text.substring(text.indexOf(filter6) + filter6.length());

                String time = text.substring(0, text.indexOf(filter6_1));

                news.add(new News(title, clickUrl, from, time));
                Log.d(TAG, title);

                Log.d(TAG, clickUrl + " - from : " + from + ", " + time);
            }

            return news;
        }
    }
}