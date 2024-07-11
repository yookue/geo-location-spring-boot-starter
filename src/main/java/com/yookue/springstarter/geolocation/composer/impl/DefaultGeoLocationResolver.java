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

package com.yookue.springstarter.geolocation.composer.impl;


import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.yookue.commonplexus.javaseutil.constant.CharVariantConst;
import com.yookue.commonplexus.javaseutil.util.InetAddressWraps;
import com.yookue.springstarter.geolocation.composer.GeoLocationResolver;
import lombok.AllArgsConstructor;


/**
 * Composer implementation for {@link com.maxmind.geoip2.DatabaseReader} and {@link com.maxmind.geoip2.WebServiceClient}
 *
 * @author David Hsing
 * @see com.maxmind.geoip2.DatabaseReader
 * @see com.maxmind.geoip2.WebServiceClient
 */
@AllArgsConstructor
@SuppressWarnings("unused")
public class DefaultGeoLocationResolver implements GeoLocationResolver {
    private DatabaseReader countryReader;
    private DatabaseReader cityReader;
    private WebServiceClient webClient;
    private boolean discardLan;

    @Override
    public String getCompositeAddress(@Nullable String ipAddress) throws IOException, GeoIp2Exception {
        return getCompositeAddress(ipAddress, (Locale) null);
    }

    @Override
    public String getCompositeAddress(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception {
        return getCompositeAddress(ipAddress, CharVariantConst.SLASH, locale);
    }

    @Override
    public String getCompositeAddress(@Nullable String ipAddress, char delimiter) throws IOException, GeoIp2Exception {
        return getCompositeAddress(ipAddress, delimiter, null);
    }

    @Override
    public String getCompositeAddress(@Nullable String ipAddress, char delimiter, @Nullable Locale locale) throws IOException, GeoIp2Exception {
        return getCompositeAddress(ipAddress, CharUtils.toString(delimiter), locale);
    }

    @Override
    public String getCompositeAddress(@Nullable String ipAddress, @Nullable String delimiter) throws IOException, GeoIp2Exception {
        return getCompositeAddress(ipAddress, delimiter, null);
    }

    @Override
    public String getCompositeAddress(@Nullable String ipAddress, @Nullable String delimiter, @Nullable Locale locale) throws IOException, GeoIp2Exception {
        if (StringUtils.isBlank(ipAddress) || (discardLan && InetAddressWraps.isLanAddress(ipAddress)) || ObjectUtils.allNull(countryReader, cityReader, webClient)) {
            return null;
        }
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String countryName = null, cityName = null;
        if (countryReader != null) {
            Optional<CountryResponse> countryResponse = countryReader.tryCountry(inetAddress);
            if (countryResponse != null && countryResponse.isPresent()) {
                countryName = getLocalizedCountryName(countryResponse.get(), locale);
            }
        }
        if (cityReader != null) {
            Optional<CityResponse> cityResponse = cityReader.tryCity(inetAddress);
            if (cityResponse != null && cityResponse.isPresent()) {
                cityName = getLocalizedCityName(cityResponse.get(), locale);
            }
        }
        if (ObjectUtils.anyNotNull(countryReader, cityReader)) {
            return StringUtils.isAllBlank(countryName, cityName) ? null : StringUtils.joinWith(StringUtils.defaultString(delimiter), countryName, cityName);
        }
        if (webClient != null) {
            CountryResponse countryResponse = webClient.country(inetAddress);
            if (countryResponse != null) {
                countryName = getLocalizedCountryName(countryResponse, locale);
            }
            CityResponse cityResponse = webClient.city(inetAddress);
            if (cityResponse != null) {
                cityName = getLocalizedCityName(cityResponse, locale);
            }
            return StringUtils.isAllBlank(countryName, cityName) ? null : StringUtils.joinWith(StringUtils.defaultString(delimiter), countryName, cityName);
        }
        return null;
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress) {
        return getCompositeAddressQuietly(ipAddress, (Locale) null);
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable Locale locale) {
        return getCompositeAddressQuietly(ipAddress, CharVariantConst.SLASH, locale);
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress, char delimiter) {
        return getCompositeAddressQuietly(ipAddress, delimiter, null);
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress, char delimiter, @Nullable Locale locale) {
        return getCompositeAddressQuietly(ipAddress, CharUtils.toString(delimiter), locale);
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable String delimiter) {
        return getCompositeAddressQuietly(ipAddress, delimiter, null);
    }

    @Override
    public String getCompositeAddressQuietly(@Nullable String ipAddress, @Nullable String delimiter, @Nullable Locale locale) {
        try {
            return getCompositeAddress(ipAddress, delimiter, locale);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String getCountryName(@Nullable String ipAddress) throws IOException, GeoIp2Exception {
        return getCountryName(ipAddress, null);
    }

    @Override
    public String getCountryName(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception {
        if (StringUtils.isBlank(ipAddress) || (discardLan && InetAddressWraps.isLanAddress(ipAddress)) || ObjectUtils.allNull(countryReader, webClient)) {
            return null;
        }
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        if (countryReader != null) {
            Optional<CountryResponse> countryResponse = countryReader.tryCountry(inetAddress);
            if (countryResponse != null && countryResponse.isPresent()) {
                return getLocalizedCountryName(countryResponse.get(), locale);
            }
        }
        if (webClient != null) {
            CountryResponse countryResponse = webClient.country(inetAddress);
            return getLocalizedCountryName(countryResponse, locale);
        }
        return null;
    }

    @Override
    public String getCountryNameQuietly(@Nullable String ipAddress) {
        return getCountryNameQuietly(ipAddress, null);
    }

    @Override
    public String getCountryNameQuietly(@Nullable String ipAddress, @Nullable Locale locale) {
        try {
            return getCountryName(ipAddress, locale);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String getCityName(@Nullable String ipAddress) throws IOException, GeoIp2Exception {
        return getCityName(ipAddress, null);
    }

    @Override
    public String getCityName(@Nullable String ipAddress, @Nullable Locale locale) throws IOException, GeoIp2Exception {
        if (StringUtils.isBlank(ipAddress) || (discardLan && InetAddressWraps.isLanAddress(ipAddress)) || ObjectUtils.allNull(cityReader, webClient)) {
            return null;
        }
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        if (cityReader != null) {
            Optional<CityResponse> cityResponse = cityReader.tryCity(inetAddress);
            if (cityResponse != null && cityResponse.isPresent()) {
                return getLocalizedCityName(cityResponse.get(), locale);
            }
        }
        if (webClient != null) {
            CityResponse cityResponse = webClient.city(inetAddress);
            return getLocalizedCityName(cityResponse, locale);
        }
        return null;
    }

    @Override
    public String getCityNameQuietly(@Nullable String ipAddress) {
        return getCityNameQuietly(ipAddress, null);
    }

    @Override
    public String getCityNameQuietly(@Nullable String ipAddress, @Nullable Locale locale) {
        try {
            return getCityName(ipAddress, locale);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Nullable
    private String getLocalizedCountryName(@Nullable CountryResponse response, @Nullable Locale locale) {
        if (response == null) {
            return null;
        }
        return locale == null ? response.getCountry().getName() : extractLocalizedName(response.getCountry().getNames(), locale);
    }

    @Nullable
    private String getLocalizedCityName(@Nullable CityResponse response, @Nullable Locale locale) {
        if (response == null) {
            return null;
        }
        return locale == null ? response.getCity().getName() : extractLocalizedName(response.getCity().getNames(), locale);
    }

    @Nullable
    private String extractLocalizedName(@Nullable Map<String, String> names, @Nonnull Locale locale) {
        if (names == null || names.isEmpty()) {
            return null;
        }
        List<Locale> lookups = LocaleUtils.localeLookupList(locale);
        for (Locale lookup : lookups) {
            if (names.containsKey(lookup.toLanguageTag())) {
                return names.get(lookup.toLanguageTag());
            }
        }
        return null;
    }
}
