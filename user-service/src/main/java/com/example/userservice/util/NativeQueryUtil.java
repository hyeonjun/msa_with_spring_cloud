package com.example.userservice.util;

import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.PositionalParameterInformation;

public class NativeQueryUtil {

  private NativeQueryUtil() {

  }

  private static QueryTranslatorImpl getQueryTranslator(
    EntityManager entityManager, JPQLSerializer serializer) {
    // JPQL -> SQL
    String hql = serializer.toString();
    ASTQueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
    SessionFactory hibernateSession = entityManager.getEntityManagerFactory()
      .unwrap(SessionFactory.class);
    QueryTranslatorImpl queryTranslator = (QueryTranslatorImpl) translatorFactory
      .createQueryTranslator("", hql, Collections.emptyMap(),
        (SessionFactoryImplementor) hibernateSession, null);
    queryTranslator.compile(Collections.emptyMap(), false);

    return queryTranslator;
  }

  private static <E> JPQLSerializer getSerializer(EntityManager entityManager, JPQLQuery<E> jpqlQuery) {
    JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT, entityManager);
    serializer.serialize(jpqlQuery.getMetadata(), false, null);
    return serializer;
  }

  private static Object[] getParameterList(JPQLSerializer serializer, QueryTranslatorImpl queryTranslator) {
    // Make Parameter (JPQLSerializer QueryTranslator 내 Parameter Map 이용)
    Iterator<Entry<Object, String>> constantToAllLabelsIterator = serializer.getConstantToLabel().entrySet().iterator();
    Map<String, Object> paramMapLabelToValue = new HashMap<>();

    while (constantToAllLabelsIterator.hasNext()) {
      Entry<Object, String> entry = constantToAllLabelsIterator.next();
      paramMapLabelToValue.put(entry.getValue(), entry.getKey());
    }

    Iterator<Entry<Integer, PositionalParameterInformation>> positionalParameterInformationMapIterator = queryTranslator
      .getParameterTranslations().getPositionalParameterInformationMap().entrySet().iterator();
    Object[] paramList = new Object[queryTranslator.getCollectedParameterSpecifications().size()];

    while (positionalParameterInformationMapIterator.hasNext()) {
      Entry<Integer, PositionalParameterInformation> entry = positionalParameterInformationMapIterator
        .next();
      PositionalParameterInformation positionalParameterInformation = entry.getValue();
      Object parameter = paramMapLabelToValue
        .get(String.valueOf(positionalParameterInformation.getLabel()));
      int[] location = positionalParameterInformation.getSourceLocations();
      for (int i1 : location) {
        if (parameter instanceof Enum) {
          parameter = parameter.toString();
        }
        paramList[i1] = parameter;
      }
    }
    return paramList;
  }

  public static Query toNativeCountQuery(JPAQuery<?> jpaQuery, EntityManager em) {
    JPQLSerializer serializer = getSerializer(em, jpaQuery);
    QueryTranslatorImpl queryTranslator = getQueryTranslator(em, serializer);

    // Make SQL to Native Query without binding parameter
    String countSql = convertToCountQueryString(queryTranslator);
    Query nativeQuery = em.createNativeQuery(countSql);

    // Binding Parameter
    Object[] paramList = getParameterList(serializer, queryTranslator);
    for (int i = 0; i < paramList.length; i++) {
      nativeQuery.setParameter(i + 1, paramList[i]);
    }
    return nativeQuery;
  }

  public static String convertToCountQueryString(QueryTranslatorImpl queryTranslator) {
    String sql = "select count(1) from (" + queryTranslator.getSQLString() + ") _tmp_"
      + System.currentTimeMillis();
    String[] sqlList = sql.split("\\?");
    int size = sqlList.length;
    StringBuilder countSQL = new StringBuilder();
    int i = 0;
    for (String s : sqlList) {
      i++;
      countSQL.append(s);
      if (size != i) {
        countSQL.append("?");
        countSQL.append(i);
      }
    }
    return countSQL.toString();
  }

}

