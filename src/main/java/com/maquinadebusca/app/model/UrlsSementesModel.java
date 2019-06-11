package com.maquinadebusca.app.model;

import java.util.List;
import java.util.LinkedList;

public class UrlsSementesModel {

  private List<String> urls = new LinkedList ();

  public UrlsSementesModel () {
  }

  public List<String> getUrls () {
    return urls;
  }

  public void setUrls (List<String> urls) {
    this.urls = urls;
  }

}
