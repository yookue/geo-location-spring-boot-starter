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

package com.yookue.springstarter.geolocation;


import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.yookue.commonplexus.javaseutil.util.InetAddressWraps;
import com.yookue.commonplexus.javaseutil.util.OptionalPlainWraps;
import com.yookue.commonplexus.javaseutil.util.StackTraceWraps;
import com.yookue.springstarter.geolocation.config.GeoLocationAutoConfiguration;
import lombok.extern.slf4j.Slf4j;


@SpringBootTest(classes = MockApplicationInitializer.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
class MockApplicationTest {
    @Autowired(required = false)
    @Qualifier(value = GeoLocationAutoConfiguration.COUNTRY_DATABASE_READER)
    private DatabaseReader countryReader;

    @Autowired(required = false)
    @Qualifier(value = GeoLocationAutoConfiguration.CITY_DATABASE_READER)
    private DatabaseReader cityReader;

    @Autowired(required = false)
    @Qualifier(value = GeoLocationAutoConfiguration.WEB_SERVICE_CLIENT)
    private WebServiceClient webServiceClient;

    private static final String IP_ADDRESS = "128.101.101.101";    // $NON-NLS-1$
    private static final InetAddress INET_ADDRESS = InetAddressWraps.getByName(IP_ADDRESS);

    @Test
    void countryLocal() throws IOException, GeoIp2Exception {
        Assertions.assertNotNull(countryReader, "Local city reader can not be null");
        String methodName = StackTraceWraps.getExecutingMethodName();
        Optional<CountryResponse> countryResponse = countryReader.tryCountry(INET_ADDRESS);
        OptionalPlainWraps.ifPresent(countryResponse, element -> log.info("{} = IP '{}' country is '{}'", methodName, IP_ADDRESS, element.getCountry().getName()));
    }

    @Test
    void cityLocal() throws IOException, GeoIp2Exception {
        Assertions.assertNotNull(cityReader, "Local city reader can not be null");
        String methodName = StackTraceWraps.getExecutingMethodName();
        Optional<CityResponse> cityResponse = cityReader.tryCity(INET_ADDRESS);
        OptionalPlainWraps.ifPresent(cityResponse, element -> log.info("{} = IP '{}' city is {}", methodName, IP_ADDRESS, element.getCity().getName()));
    }

    @Test
    void countryRemote() throws IOException, GeoIp2Exception {
        Assertions.assertNotNull(webServiceClient, "Remote WebServiceClient can not be null");
        String methodName = StackTraceWraps.getExecutingMethodName();
        CountryResponse countryResponse = webServiceClient.country(INET_ADDRESS);
        Optional.ofNullable(countryResponse).ifPresent(element -> log.info("{} = IP '{}' country is '{}'", methodName, IP_ADDRESS, element.getCountry().getName()));
    }

    @Test
    void cityRemote() throws IOException, GeoIp2Exception {
        Assertions.assertNotNull(webServiceClient, "Remote WebServiceClient can not be null");
        String methodName = StackTraceWraps.getExecutingMethodName();
        CityResponse cityResponse = webServiceClient.city(INET_ADDRESS);
        Optional.ofNullable(cityResponse).ifPresent(element -> log.info("{} = IP '{}' city is '{}'", methodName, IP_ADDRESS, element.getCity().getName()));
    }
}
