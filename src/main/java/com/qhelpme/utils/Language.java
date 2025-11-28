package com.qhelpme.utils;

public enum Language {
    EN,
    FA;

    public static Language fromConfig(String value) {
        if (value == null) {
            return EN;
        }
        try {
            return Language.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return EN;
        }
    }
}


