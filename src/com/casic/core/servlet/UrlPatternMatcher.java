 package com.casic.core.servlet;
 
 public abstract class UrlPatternMatcher
 {
   public static final UrlPatternMatcher DEFAULT_MATCHER = new AllPassUrlPatternMatcher();
   private String urlPattern;
 
   public static UrlPatternMatcher create(String urlPattern)
   {
     UrlPatternMatcher urlPatternMatcher;
     
     if (urlPattern.equals("/*")) {
       urlPatternMatcher = DEFAULT_MATCHER;
     }
     else
     {
       if (urlPattern.startsWith("*")) {
         String suffix = urlPattern.substring(1);
 
         urlPatternMatcher = new SuffixUrlPatternMatcher(suffix);
       }
       else
       {
         if (urlPattern.endsWith("*")) {
           String prefix = urlPattern.substring(0, urlPattern.length() - 1);
 
           urlPatternMatcher = new PrefixUrlPatternMatcher(prefix);
         } else {
           urlPatternMatcher = new EqualsUrlPatternMatcher(urlPattern);
         }
       }
     }
     urlPatternMatcher.setUrlPattern(urlPattern);
 
     return urlPatternMatcher;
   }
 
   public abstract boolean matches(String paramString);
 
   public boolean shouldRedirect(String url) {
     return false;
   }
 
   public String getUrlPattern() {
     return this.urlPattern;
   }
 
   public void setUrlPattern(String urlPattern) {
     this.urlPattern = urlPattern;
   }
 
   static class AllPassUrlPatternMatcher extends UrlPatternMatcher
   {
     public boolean matches(String url)
     {
       return true;
     }
   }
 
   static class EqualsUrlPatternMatcher extends UrlPatternMatcher
   {
     private String urlPattern;
 
     public EqualsUrlPatternMatcher(String urlPattern)
     {
       this.urlPattern = urlPattern;
     }
 
     public boolean matches(String url) {
       return url.equals(this.urlPattern);
     }
   }
 
   static class PrefixUrlPatternMatcher extends UrlPatternMatcher
   {
     private String withoutSlash;
     private String prefix;
 
     public PrefixUrlPatternMatcher(String prefix)
     {
       this.prefix = prefix;
 
       if (prefix.endsWith("/"))
         this.withoutSlash = prefix.substring(0, prefix.length() - 1);
       else
         this.withoutSlash = prefix;
     }
 
     public boolean matches(String url)
     {
       return (shouldRedirect(url)) || (url.startsWith(this.prefix));
     }
 
     public boolean shouldRedirect(String url) {
       return url.equals(this.withoutSlash);
     }
   }
 
   static class SuffixUrlPatternMatcher extends UrlPatternMatcher
   {
     private String suffix;
 
     public SuffixUrlPatternMatcher(String suffix)
     {
       this.suffix = suffix;
     }
 
     public boolean matches(String url) {
       return url.endsWith(this.suffix);
     }
   }
 }

