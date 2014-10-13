package com.casic.alarm.permission;

import java.util.List;
import java.util.Map;

public abstract interface UrlSourceFetcher
{
  public abstract List<String> getSource(String paramString);
}