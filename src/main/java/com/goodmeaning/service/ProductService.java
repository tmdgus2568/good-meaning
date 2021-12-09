package com.goodmeaning.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private static String URL = "https://thepicker.net/LIVING";

	@PostConstruct
	public void getProductDatas() throws IOException {

		Document doc = Jsoup.connect(URL).get();
		// Elements contents = doc.select(".item-wrap");
		// 상품명 Elements contents = doc.select(".item-pay h2");
		// 가격 Elements contents = doc.select(".item-pay .inline-blocked");
		Elements contents = doc.getElementsByAttributeValue("class", "_org_img org_img _lazy_img");
		Elements procontents = doc.select(".item-pay ._fade_link");
		Elements pricecontents = doc.select(".item-pay .inline-blocked");
		System.out.println(procontents.size());
		for (int i = 0; i < contents.size(); i++) {
			String content = contents.get(i).attr("src");
			Element name = procontents.get(i);
			Element price = pricecontents.get(i);
			String nameStr = name.text();
			String priceStr = price.text();
			//System.out.println(content);
			//System.out.println(nameStr);
			//System.out.println(priceStr);
		}
	}
}
