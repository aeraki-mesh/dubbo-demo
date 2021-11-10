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

package org.apache.dubbo.samples.basic;

import java.io.IOException;

import org.apache.dubbo.samples.basic.api.DemoService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.dubbo.rpc.RpcContext;

public class BasicConsumer {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-demo-consumer.xml");
        context.start();

        startTestServer();

        if(args.length>0 && args[0].equals("demo")) {
            System.out.println("Periodically call dubbo server");
            RpcContext.getContext().setAttachment("batchJob", args[1]);
            RpcContext.getContext().setAttachment("foo", "bar");
            while (true) {
                try {
                    Thread.sleep(5000);
                    DemoService demoService = (DemoService) context.getBean("demoService");
                    String hello = demoService.sayHello("Aeraki");
                    System.out.println(hello);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * startTestServer starts a HTTP server for e2e test
     */
    static void startTestServer() {
        Thread serverThread = new Thread(){
            public void run(){
                System.out.println("Start a http server for e2e test");
                int port = 9009;
                Server server = new Server(port);
                ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
                context.setContextPath("/");
                ServletHolder h = new ServletHolder(new HttpServletDispatcher());
                h.setInitParameter("javax.ws.rs.Application", "com.embedded.Services");
                context.addServlet(h, "/*");
                server.setHandler(context);
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serverThread.start();
    }
}
