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

package com.yookue.springstarter.geolocation.property;


import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import com.yookue.springstarter.geolocation.config.GeoLocationAutoConfiguration;
import com.yookue.springstarter.geolocation.enumeration.GeoCacheType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for GEO location
 *
 * @author David Hsing
 */
@ConfigurationProperties(prefix = GeoLocationAutoConfiguration.PROPERTIES_PREFIX)
@Getter
@Setter
@ToString
public class GeoLocationProperties implements Serializable {
    /**
     * Indicates whether to enable this starter or not
     * <p>
     * Default is {@code true}
     */
    private Boolean enabled = true;

    /**
     * Indicates whether to discard lan address or not
     */
    private Boolean discardLan = true;

    /**
     * Local file attributes
     */
    private final LocalFile localFile = new LocalFile();

    /**
     * Remote site attributes
     */
    private final RemoteSite remoteSite = new RemoteSite();


    /**
     * Properties for local file
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class LocalFile implements Serializable {
        /**
         * Indicates whether to enable local file or not
         * <p>
         * Default is {@code true}
         */
        private Boolean enabled = true;

        /**
         * Country database resource path
         * <p>
         * If under the classpath, should specify with prefix "classpath:"
         */
        private String countryDb;

        /**
         * City database resource path
         * <p>
         * If under the classpath, should specify with prefix "classpath:"
         */
        private String cityDb;

        /**
         * ASN database resource path
         * <p>
         * If under the classpath, should specify with prefix "classpath:"
         */
        private String asnDb;

        /**
         * Preferred locales for results
         */
        private List<String> locales;

        /**
         * Database cache type
         * <p>
         * Default is {@code CHM}
         */
        private GeoCacheType cacheType = GeoCacheType.CHM;

        /**
         * Database cache capacity
         */
        private Integer cacheCapacity;
    }


    /**
     * Properties for remote site
     *
     * @author David Hsing
     * @see com.maxmind.geoip2.WebServiceClient.Builder
     */
    @Getter
    @Setter
    @ToString
    public static class RemoteSite implements Serializable {
        /**
         * Indicates whether to enable remote site or not
         * <p>
         * Default is {@code true}
         */
        private Boolean enabled = true;

        /**
         * Account ID of the remote site
         */
        private Integer accountId;

        /**
         * License key of the remote site
         */
        private String licenseKey;

        /**
         * Serving host of the remote site
         * <p>
         * Default is "geolite.info"
         */
        private String host = "geolite.info";    // $NON-NLS-1$

        /**
         * Serving port of the remote site
         */
        private Integer port;

        /**
         * Indicates whether to connect the remote site with SSL protocol or not
         * <p>
         * Default is {@code true}
         */
        private Boolean useHttps = true;

        /**
         * Preferred locales for results
         */
        private List<String> locales;

        /**
         * Connect timeout duration for the remote site
         * <p>
         * Default is 30 seconds
         */
        @DurationUnit(value = ChronoUnit.SECONDS)
        private Duration connectTimeout = Duration.ofSeconds(30L);

        /**
         * Request timeout duration for the remote site
         * <p>
         * Default is 30 seconds
         */
        @DurationUnit(value = ChronoUnit.SECONDS)
        private Duration requestTimeout = Duration.ofSeconds(30L);

        /**
         * Proxy host for the remote site
         */
        private String proxyHost;

        /**
         * Proxy port for the remote site
         */
        private Integer proxyPort;
    }
}
