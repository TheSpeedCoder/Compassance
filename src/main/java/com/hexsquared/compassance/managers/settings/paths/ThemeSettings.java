package com.hexsquared.compassance.managers.settings.paths;

public class ThemeSettings
{
    public static final String THEME_META_NAME = "themes.%s.meta.name";
    public static final String THEME_META_DESC = "themes.%s.meta.desc";

    public static final String THEME_DATA_MAIN_PATTERN_MAP = "themes.%s.data.main-pattern-map";

    public static final String THEME_DATA_DIRECT_REPLACER = "themes.%s.data.direct.%s"; //id,direction-id

    public static final String THEME_DATA_SUBPATTERN_MAP = "themes.%s.data.sub-pattern.%s.pattern-map"; //id,sep-id
    public static final String THEME_DATA_SUBPATTERN_MAP_REPLACER = "themes.%s.data.sub-pattern.%s.%s"; //id,sep-id,replacer-id

    public static final String THEME_FINAL_MAIN_PATTERN_MAP = "themes.%s.final.pattern-map";
}
