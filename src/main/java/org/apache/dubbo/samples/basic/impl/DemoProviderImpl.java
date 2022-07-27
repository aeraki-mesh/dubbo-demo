/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.apache.dubbo.samples.basic.impl;

import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.samples.basic.api.ComplexService;
import org.apache.dubbo.samples.basic.api.DemoService;
import org.apache.dubbo.samples.basic.api.TestService;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.dubbo.rpc.RpcContext;

public class DemoProviderImpl implements DemoService,TestService, ComplexService {

    @Override
    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        Map<String, String> attachments = RpcContext.getContext().getAttachments();
        System.out.println("Message headers:");
        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            System.out.println("      key: " + entry.getKey() + " value: " + entry.getValue());
        }
        String address = "";
        try {
            address = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "Hello " + name + ", response from " + address;
    }

    @Override
    public void testVoid() {

    }
}
