package com.hzk.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class JndiClientTest {

    public static void main(String[] args) throws Exception {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:1389");

        Context ctx = new InitialContext(env);
        Object obj = ctx.lookup("cn=test,dc=test,dc=local");

        System.out.println(obj);
    }
}

