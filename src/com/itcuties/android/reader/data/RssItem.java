package com.itcuties.android.reader.data;

/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 * 
 * @author ITCuties
 *
 */
public class RssItem {
	
	// item title
	private String title;
	// item link
	private String link;
	private String  pubDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public String getpubDate() {
		return pubDate;
	}

	public void setpubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
