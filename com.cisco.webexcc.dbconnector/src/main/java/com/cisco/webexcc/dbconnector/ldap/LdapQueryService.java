package com.cisco.webexcc.dbconnector.ldap;

import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LdapQueryService {

    public List<Map<String, Object>> search(
            String url,
            String bindDn,
            String bindPassword,
            String baseDn,
            String filter,
            List<String> filterArgs,
            List<String> attributes,
            int sizeLimit,
            int timeLimitMs,
            String scope
    ) throws NamingException {
        Hashtable<String, Object> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, bindDn);
        env.put(Context.SECURITY_CREDENTIALS, bindPassword);

        SearchControls controls = new SearchControls();
        controls.setSearchScope(toSearchScope(scope));
        controls.setCountLimit(Math.max(sizeLimit, 1));
        controls.setTimeLimit(Math.max(timeLimitMs, 1));

        if (attributes != null && !attributes.isEmpty()) {
            controls.setReturningAttributes(attributes.toArray(String[]::new));
        }

        List<Map<String, Object>> results = new ArrayList<>();

        InitialLdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(env, null);
            Object[] args = filterArgs == null ? new Object[0] : filterArgs.toArray();
            NamingEnumeration<SearchResult> queryResults = ctx.search(baseDn, filter, args, controls);

            while (queryResults.hasMore()) {
                SearchResult entry = queryResults.next();
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("_dn", entry.getNameInNamespace());

                NamingEnumeration<? extends Attribute> allAttrs = entry.getAttributes().getAll();
                while (allAttrs.hasMore()) {
                    Attribute attribute = allAttrs.next();
                    List<Object> values = new ArrayList<>();
                    NamingEnumeration<?> allValues = attribute.getAll();
                    while (allValues.hasMore()) {
                        values.add(allValues.next());
                    }
                    row.put(attribute.getID(), values.size() == 1 ? values.get(0) : values);
                }

                results.add(row);
            }
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }

        return results;
    }

    private int toSearchScope(String scope) {
        if (scope == null) {
            return SearchControls.SUBTREE_SCOPE;
        }
        return switch (scope.toLowerCase(Locale.ROOT)) {
            case "object" -> SearchControls.OBJECT_SCOPE;
            case "onelevel" -> SearchControls.ONELEVEL_SCOPE;
            default -> SearchControls.SUBTREE_SCOPE;
        };
    }
}