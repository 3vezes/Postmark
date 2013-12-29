package com.ericrgon.postmark.model;


public class License {

    private final int header;
    private final int copyright;
    private final int license;

    public License(int header, int copyright, int license) {
        this.header = header;
        this.copyright = copyright;
        this.license = license;
    }

    public static License newInstance(int header,int copyright,int license){
        return new License(header,copyright,license);
    }

    public int getHeader() {
        return header;
    }

    public int getCopyright() {
        return copyright;
    }

    public int getLicense() {
        return license;
    }
}
