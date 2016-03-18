package com.pegadaian.aplikasi.cobalagi.service;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

/**
 * Created by opaw on 3/18/16.
 */
public class HttpUtils {
    public static CloseableHttpClient getNewHttpClient() {
        try {

            SSLContext sslcontext = buildSSLContext();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            CloseableHttpClient client = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();

            return client;
        } catch (Exception e) {
            return null;
        }
    }

    private static SSLContext buildSSLContext()
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException {
        SSLContext sslcontext = SSLContexts.custom()
                .setSecureRandom(new SecureRandom())
                .loadTrustMaterial(null, new TrustStrategy() {

                    public boolean isTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        return true;
                    }
                })
                .build();
        return sslcontext;
    }
}
