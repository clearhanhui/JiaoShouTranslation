/**
  * Copyright 2018 bejson.com 
  */
package com.hanhui.jiaoshoutranslation.jinshan_yh;
import java.util.List;

/**
 * Auto-generated: 2018-07-20 0:28:44
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String word_name;
    private int is_CRI;
//    private Exchange exchange;
    private List<Symbols> symbols;
    private List<String> items;
    public void setWord_name(String word_name) {
         this.word_name = word_name;
     }
     public String getWord_name() {
         return word_name;
     }

    public void setIs_CRI(int is_CRI) {
         this.is_CRI = is_CRI;
     }
     public int getIs_CRI() {
         return is_CRI;
     }

//    public void setExchange(Exchange exchange) {
//         this.exchange = exchange;
//     }
//     public Exchange getExchange() {
//         return exchange;
//     }

    public void setSymbols(List<Symbols> symbols) {
         this.symbols = symbols;
     }
     public List<Symbols> getSymbols() {
         return symbols;
     }

    public void setItems(List<String> items) {
         this.items = items;
     }
     public List<String> getItems() {
         return items;
     }

}