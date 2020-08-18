package com.example.demo.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.stream.Stream;

public class CustomerIdGenerator implements IdentifierGenerator {
    
    private String prefix = "GAUNAU";
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session,
        Object obj) throws HibernateException {
        String query = "SELECT e.id FROM Customer e";
        Stream<String> ids = session.createQuery(query, String.class).stream();
        
        long max = ids.map(o -> o.replace(prefix, "")).mapToLong(Long::parseLong).max().orElse(0L);
        
        return prefix + (String.format("%04d", max + 1));
    }
}
