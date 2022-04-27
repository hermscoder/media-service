package com.shareit.utils.commons.security;

public class AntMatcher {
    private String[] publicMatchers;
    private String[] publicMatchersGet;
    private String[] publicMatchersPost;
    private String[] publicMatchersDelete;
    private String[] publicMatchersPut;

    private AntMatcher(String[] publicMatchers, String[] publicMatchersGet, String[] publicMatchersPost, String[] publicMatchersDelete, String[] publicMatchersPut) {
        this.publicMatchers = publicMatchers;
        this.publicMatchersGet = publicMatchersGet;
        this.publicMatchersPost = publicMatchersPost;
        this.publicMatchersDelete = publicMatchersDelete;
        this.publicMatchersPut = publicMatchersPut;
    }

    public String[] getPublicMatchers() {
        return publicMatchers;
    }

    public String[] getPublicMatchersGet() {
        return publicMatchersGet;
    }

    public String[] getPublicMatchersPost() {
        return publicMatchersPost;
    }

    public String[] getPublicMatchersDelete() {
        return publicMatchersDelete;
    }

    public String[] getPublicMatchersPut() {
        return publicMatchersPut;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String[] publicMatchers = new String[0];
        private String[] publicMatchersGet = new String[0];
        private String[] publicMatchersPost = new String[0];
        private String[] publicMatchersDelete = new String[0];
        private String[] publicMatchersPut = new String[0];

        private Builder(){

        }
        public AntMatcher build() {
            return new AntMatcher(publicMatchers, publicMatchersGet, publicMatchersPost, publicMatchersDelete, publicMatchersPut);
        }

        public Builder publicMatchers(String[] publicMatchers) {
            this.publicMatchers = publicMatchers;
            return this;
        }

        public Builder publicMatchersGet(String[] publicMatchersGet) {
            this.publicMatchersGet = publicMatchersGet;
            return this;
        }

        public Builder publicMatchersPost(String[] publicMatchersPost) {
            this.publicMatchersPost = publicMatchersPost;
            return this;
        }

        public Builder publicMatchersDelete(String[] publicMatchersDelete) {
            this.publicMatchersDelete = publicMatchersDelete;
            return this;
        }

        public Builder publicMatchersPut(String[] publicMatchersPut) {
            this.publicMatchersPut = publicMatchersPut;
            return this;
        }
    }
}
