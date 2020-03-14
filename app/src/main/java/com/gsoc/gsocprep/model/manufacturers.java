package com.gsoc.gsocprep.model;

import java.util.ArrayList;

public class manufacturers {
    ArrayList < Object > links = new ArrayList< Object >();
    private String hsn;
    private String name;


    // Getter Methods

    public manufacturers(String hsn, String name) {
        this.hsn = hsn;
        this.name = name;
    }

    public String getHsn() {
        return hsn;
    }

    public String getName() {
        return name;
    }

    // Setter Methods

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public void setName(String name) {
        this.name = name;
    }

}
