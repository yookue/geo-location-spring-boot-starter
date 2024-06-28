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

package com.yookue.springstarter.geolocation.enumeration;


import com.yookue.commonplexus.javaseutil.support.ValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Enumerations of GEO database types
 *
 * @author David Hsing
 * @see com.maxmind.geoip2.DatabaseReader.DatabaseType
 */
@AllArgsConstructor
@Getter
@SuppressWarnings({"unused", "JavadocReference"})
public enum GeoDatabaseType implements ValueEnum<String> {
    ANONYMOUS_IP("anonymous-ip"),    // $NON-NLS-1$
    ASN("asn"),    // $NON-NLS-1$
    CITY("city"),    // $NON-NLS-1$
    CONNECTION_TYPE("connection-type"),    // $NON-NLS-1$
    COUNTRY("country"),    // $NON-NLS-1$
    DOMAIN("domain"),    // $NON-NLS-1$
    ENTERPRISE("enterprise"),    // $NON-NLS-1$
    ISP("isp");    // $NON-NLS-1$

    private final String value;
}
