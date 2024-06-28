/*
 * Copyright (c) 2020 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.geolocation.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import com.maxmind.db.CHMCache;
import com.maxmind.db.NoCache;
import com.maxmind.geoip2.DatabaseReader;
import com.yookue.commonplexus.javaseutil.util.NumberUtilsWraps;
import com.yookue.commonplexus.springutil.util.ResourceUtilsWraps;
import com.yookue.springstarter.geolocation.enumeration.GeoDatabaseType;
import com.yookue.springstarter.geolocation.property.GeoLocationProperties;


/**
 * Utilities for GEO {@link com.maxmind.geoip2.DatabaseReader}
 *
 * @author David Hsing
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class GeoDatabaseUtils {
    @Nonnull
    public static DatabaseReader getDatabaseReader(@Nonnull GeoLocationProperties properties, @Nonnull GeoDatabaseType type) throws IOException {
        GeoLocationProperties.LocalFile localFile = properties.getLocalFile();
        String location = null;
        switch (type) {
            case COUNTRY:
                location = localFile.getCountryDb();
                break;
            case CITY:
                location = localFile.getCityDb();
                break;
            case ASN:
                location = localFile.getAsnDb();
                break;
            default:
                break;
        }
        Resource resource = ResourceUtilsWraps.determineResource(location);
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("Location database is not exists or readable");
        }
        DatabaseReader.Builder builder = new DatabaseReader.Builder(resource.getInputStream());
        if (!CollectionUtils.isEmpty(localFile.getLocales())) {
            builder.locales(localFile.getLocales());
        }
        switch (localFile.getCacheType()) {
            case NO:
                builder.withCache(NoCache.getInstance());
                break;
            case CHM:
                builder.withCache(NumberUtilsWraps.isPositive(localFile.getCacheCapacity()) ? new CHMCache(localFile.getCacheCapacity()) : new CHMCache());
                break;
            default:
                break;
        }
        return builder.build();
    }
}
