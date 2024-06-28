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

package com.yookue.springstarter.geolocation.config;


import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.yookue.commonplexus.javaseutil.util.InetProxyWraps;
import com.yookue.commonplexus.javaseutil.util.NumberUtilsWraps;
import com.yookue.commonplexus.springcondition.annotation.ConditionalOnAllProperties;
import com.yookue.springstarter.geolocation.composer.GeoLocationResolver;
import com.yookue.springstarter.geolocation.composer.impl.DefaultGeoLocationResolver;
import com.yookue.springstarter.geolocation.enumeration.GeoDatabaseType;
import com.yookue.springstarter.geolocation.property.GeoLocationProperties;
import com.yookue.springstarter.geolocation.util.GeoDatabaseUtils;


/**
 * Configuration for GEO location
 *
 * @author David Hsing
 * @reference "https://dev.maxmind.com/geoip/geolite2-free-geolocation-data"
 * @reference "https://maxmind.github.io/GeoIP2-java/"
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = GeoLocationProperties.class)
@ConditionalOnProperty(prefix = GeoLocationAutoConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
public class GeoLocationAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "spring.geo-location";    // $NON-NLS-1$
    public static final String COUNTRY_DATABASE_READER = "geoCountryDatabaseReader";    // $NON-NLS-1$
    public static final String CITY_DATABASE_READER = "geoCityDatabaseReader";    // $NON-NLS-1$
    public static final String ASN_DATABASE_READER = "geoAsnDatabaseReader";    // $NON-NLS-1$
    public static final String WEB_SERVICE_CLIENT = "geoWebServiceClient";    // $NON-NLS-1$
    public static final String LOCATION_RESOLVER = "geoLocationResolver";    // $NON-NLS-1$

    @Bean(name = COUNTRY_DATABASE_READER)
    @ConditionalOnMissingBean(name = COUNTRY_DATABASE_READER)
    @ConditionalOnAllProperties(value = {
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "enabled", havingValue = "true", matchIfMissing = true),
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "country-db")
    })
    public DatabaseReader countryDatabaseReader(@Nonnull GeoLocationProperties properties) throws IOException {
        return GeoDatabaseUtils.getDatabaseReader(properties, GeoDatabaseType.COUNTRY);
    }

    @Bean(name = CITY_DATABASE_READER)
    @ConditionalOnMissingBean(name = CITY_DATABASE_READER)
    @ConditionalOnAllProperties(value = {
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "enabled", havingValue = "true", matchIfMissing = true),
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "city-db")
    })
    public DatabaseReader cityDatabaseReader(@Nonnull GeoLocationProperties properties) throws IOException {
        return GeoDatabaseUtils.getDatabaseReader(properties, GeoDatabaseType.CITY);
    }

    @Bean(name = ASN_DATABASE_READER)
    @ConditionalOnMissingBean(name = ASN_DATABASE_READER)
    @ConditionalOnAllProperties(value = {
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "enabled", havingValue = "true", matchIfMissing = true),
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".local-file", name = "asn-db")
    })
    public DatabaseReader asnDatabaseReader(@Nonnull GeoLocationProperties properties) throws IOException {
        return GeoDatabaseUtils.getDatabaseReader(properties, GeoDatabaseType.ASN);
    }

    @Bean(name = WEB_SERVICE_CLIENT)
    @ConditionalOnMissingBean(name = WEB_SERVICE_CLIENT)
    @ConditionalOnAllProperties(value = {
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".remote-site", name = "enabled", havingValue = "true", matchIfMissing = true),
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX + ".remote-site", name = {"account-id", "license-key"})
    })
    public WebServiceClient webServiceClient(@Nonnull GeoLocationProperties properties) {
        GeoLocationProperties.RemoteSite remoteSite = properties.getRemoteSite();
        WebServiceClient.Builder builder = new WebServiceClient.Builder(remoteSite.getAccountId(), remoteSite.getLicenseKey());
        if (StringUtils.isNotBlank(remoteSite.getHost())) {
            builder.host(remoteSite.getHost());
        }
        if (NumberUtilsWraps.isPositive(remoteSite.getPort())) {
            builder.port(remoteSite.getPort());
        }
        if (BooleanUtils.isFalse(remoteSite.getUseHttps())) {
            builder.disableHttps();
        }
        if (!CollectionUtils.isEmpty(remoteSite.getLocales())) {
            builder.locales(remoteSite.getLocales());
        }
        if (remoteSite.getConnectTimeout() != null) {
            builder.connectTimeout(DurationUtils.toMillisInt(remoteSite.getConnectTimeout()));
        }
        if (remoteSite.getRequestTimeout() != null) {
            builder.readTimeout(DurationUtils.toMillisInt(remoteSite.getRequestTimeout()));
        }
        if (StringUtils.isNotBlank(remoteSite.getProxyHost()) && NumberUtilsWraps.isPositive(remoteSite.getProxyPort())) {
            builder.proxy(InetProxyWraps.newHttpProxy(remoteSite.getProxyHost(), remoteSite.getProxyPort()));
        }
        return builder.build();
    }

    @Bean(name = LOCATION_RESOLVER)
    @ConditionalOnMissingBean(name = LOCATION_RESOLVER)
    public GeoLocationResolver locationResolver(@Nonnull GeoLocationProperties properties, @Nullable @Qualifier(value = COUNTRY_DATABASE_READER) DatabaseReader countryReader, @Nullable @Qualifier(value = CITY_DATABASE_READER) DatabaseReader cityReader, @Nullable @Qualifier(value = WEB_SERVICE_CLIENT) WebServiceClient webClient) {
        return new DefaultGeoLocationResolver(countryReader, cityReader, webClient, BooleanUtils.isTrue(properties.getDiscardLan()));
    }
}
