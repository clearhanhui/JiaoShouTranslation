/**
  * Copyright 2018 bejson.com 
  */
package com.hanhui.jiaoshoutranslation.news;
import java.util.List;

/**
 * Auto-generated: 2018-07-25 13:20:34
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String status;
    private int totalResults;
    private List<Articles> articles;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setTotalResults(int totalResults) {
         this.totalResults = totalResults;
     }
     public int getTotalResults() {
         return totalResults;
     }

    public void setArticles(List<Articles> articles) {
         this.articles = articles;
     }
     public List<Articles> getArticles() {
         return articles;
     }

}