package com.hzk.jndi;

import com.unboundid.ldap.listener.*;
import com.unboundid.ldap.sdk.*;

public class LocalLdapServer {

    public static void main(String[] args) throws Exception {

        InMemoryDirectoryServerConfig config =
                new InMemoryDirectoryServerConfig("dc=test,dc=local");

        InMemoryListenerConfig listenerConfig =
                InMemoryListenerConfig.createLDAPConfig("default", 1389);

        config.setListenerConfigs(listenerConfig);

        InMemoryDirectoryServer server =
                new InMemoryDirectoryServer(config);

        // ✅ 1. 先创建 base DN
        Entry base = new Entry("dc=test,dc=local");
        base.addAttribute("objectClass", "top", "domain");
        base.addAttribute("dc", "test");
        server.add(base);

        // ✅ 2. 再创建子节点
        Entry entry = new Entry("cn=test,dc=test,dc=local");
        entry.addAttribute("objectClass", "top", "person");
        entry.addAttribute("cn", "test");
        entry.addAttribute("sn", "jndi");
        server.add(entry);

        server.startListening();
        System.out.println("LDAP server started on port 1389");
    }
}


