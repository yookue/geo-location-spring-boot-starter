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

package com.yookue.springstarter.geolocation.composer;


import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;
import com.maxmind.geoip2.exception.GeoIp2Exception;


/**
 * Composer interface for {@link com.maxmind.geoip2.DatabaseReader} and {@link com.maxmind.geoip2.WebServiceClient}
 *
 * @author David Hsing
 */
@SuppressWarnings("unused")
public interface GeoLocationResolver {
    String getCompositeAddress(@Nullable String ipAddress) throws IOException, GeoIp2Exception;

    String getCompositeAddress(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception;

    String getCompositeAddress(@Nullable String ipAddress, char delimiter) throws IOException, GeoIp2Exception;

    String getCompositeAddress(@Nullable String ipAddress, char delimiter, @Nullable Locale locale) throws IOException, GeoIp2Exception;

    String getCompositeAddress(@Nullable String ipAddress, @Nullable String delimiter) throws IOException, GeoIp2Exception;

    String getCompositeAddress(@Nullable String ipAddress, @Nullable String delimiter, @Nullable Locale locale) throws IOException, GeoIp2Exception;

    String getCompositeAddressQuietly(@Nullable String ipAddress);

    String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable Locale locale);

    String getCompositeAddressQuietly(@Nullable String ipAddress, char delimiter);

    String getCompositeAddressQuietly(@Nullable String ipAddress, char delimiter, @Nullable Locale locale);

    String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable String delimiter);

    String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable String delimiter, @Nullable Locale locale);

    String getCountryName(@Nullable String ipAddress) throws IOException, GeoIp2Exception;

    String getCountryName(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception;

    String getCountryNameQuietly(@Nullable String ipAddress);

    String getCountryNameQuietly(@Nullable String ipAddress, @Nullable Locale locale);

    String getCityName(@Nullable String ipAddress) throws IOException, GeoIp2Exception;

    String getCityName(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception;

    String getCityNameQuietly(@Nullable String ipAddress);

    String getCityNameQuietly(@Nullable String ipAddress, @Nullable Locale locale);
}
