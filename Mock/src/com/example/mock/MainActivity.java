package com.example.mock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int MAX_QUOTES = 12802;
	private static final String SLASH = "/";
	private static final String LINK = "http://greatwords.ru/quote/";
	private static final String QUOTE = "(?<=<p class=\"greatwords\" id=\"quote-p\">).*(?=</p>)";
	private static final String AUTHOR = "(?<=\\.html\">).*(?=</a>)";
	TextView quote;
	TextView author;
	private static Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onClick(findViewById(R.id.LinearLayout01));
	}

	private String readStream(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();

		try {
			String read = br.readLine();
			while (read != null) {
				sb.append(read);
				read = br.readLine();
			}
		} catch (Exception e) {
		} finally {
			br.close();
		}
		return sb.toString();
	}

	private String parseEntry(String rawHtml, String pattern) {
		String result = null;
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(rawHtml);

		if (matcher.find()) {
			result = matcher.group();
		} else {
			result = "Author is not defined";
		}
		if (result.indexOf('<') != -1) {
			result = result.substring(0, result.indexOf('<')); // TODO fix spike
		}
		result = replaceHtmlSymbols(result);
		return result;
	}

	private String replaceHtmlSymbols(String textToReplace) {
		Map<String, String> map = HTMLsymbols.getSymbols();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			textToReplace = textToReplace.replaceAll(entry.getKey(),
					entry.getValue());
		}
		return textToReplace;
	}

	public void onClick(View view) {
		quote = (TextView) findViewById(R.id.quote);
		author = (TextView) findViewById(R.id.header);
		String response;
		try {
			URL url = new URL(LINK + random.nextInt(MAX_QUOTES) + SLASH);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			response = readStream(in);
			quote.setText(parseEntry(response, QUOTE));
			author.setText(parseEntry(response, AUTHOR));

			urlConnection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
