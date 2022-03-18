package com.core.entities;
import lombok.Getter;

import static com.utils.PropertyReader.getProperty;
import static com.utils.PropertyReader.readPropertiesFile;

public class GroupAndUserNames {
    @Getter
    public final String SITE_ADMIN_NAME;
    @Getter
    public final String SITE_ADMIN_GROUP;
    @Getter
    public final String USER_NAME;
    @Getter
    public final String USER_GROUP;

    public GroupAndUserNames() {
        readPropertiesFile();
        SITE_ADMIN_NAME = getProperty("instanceSiteAdminName");
        SITE_ADMIN_GROUP = getProperty("instanceSiteAdminGroup");
        USER_NAME = getProperty("instanceUserName");
        USER_GROUP = getProperty("instanceUserGroup");
    }


}
