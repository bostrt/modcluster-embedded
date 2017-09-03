package com.redhat.rbost.mcembed;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.jboss.modcluster.container.catalina.standalone.ModClusterListener;

/**
 * https://dzone.com/articles/embedded-tomcat-minimal
 * http://arhipov.blogspot.com/2011/03/embedded-tomcat-minimal-version.html
 * 
 * @author rbost
 *
 */
public class Main {
        public static void main(String[] args) throws LifecycleException {
                Tomcat tomcat = new Tomcat();

                // read in custom port if needed
                String port = System.getenv("PORT");
                if (port == null || port.isEmpty()) {
                        port = "8080";
                }

                tomcat.setPort(Integer.valueOf(port));

                // read in custom proxy host
                String proxyHost = System.getenv("PROXY_HOST");
                if (proxyHost == null || proxyHost.isEmpty()) {
                        proxyHost = "localhost";
                }

                // read in custom proxy port
                String proxyPortStr = System.getenv("PROXY_PORT");
                int proxyPort = 6666;
                if (proxyPortStr != null && !proxyPortStr.isEmpty()) {
                        proxyPort = Integer.parseInt(proxyPortStr);
                }

                // Setup modcluster
                ModClusterListener mcListener = new ModClusterListener();
                mcListener.addProxy(proxyHost, proxyPort);
                mcListener.setAdvertise(false);
                tomcat.getServer().addLifecycleListener(mcListener);

                // Setup a basic servlet and context
                Context ctx = tomcat.addContext("/example", new File(".").getAbsolutePath());
                Tomcat.addServlet(ctx, "hello", new HttpServlet() {
                        @Override
                        protected void service(HttpServletRequest req, HttpServletResponse resp)
                                        throws ServletException, IOException {
                                Writer w = resp.getWriter();
                                w.write("Hello!");
                                w.flush();

                        }
                });

                ctx.addServletMapping("/*", "hello");

                // go
                tomcat.start();
                tomcat.getServer().await();
        }
}
