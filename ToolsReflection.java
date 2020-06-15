package com.bas.tools;


import com.bas.pivot.EnrichedTransaction;
import com.bas.pivot.Feature;
import com.bas.pivot.Transaction;
import com.bas.state.State;
import org.javatuples.Pair;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class ToolsReflection {


    public static Object invoke(Feature feature, Transaction transaction, Pair<State, EnrichedTransaction> result, Jedis jedis, Connection connection) {
        if(feature != null) {
            try {
                //Class
                Class classToLoad = Class.forName(feature.classname);
                //Method
                Method method = classToLoad.getDeclaredMethod("compute");

                    Constructor constructor = null;
                    Object value = null;

                if("engine".equalsIgnoreCase(feature.computationtype)) {
                    constructor = classToLoad.getConstructor(Transaction.class, State.class, EnrichedTransaction.class, Feature.class);
                    Object instance = constructor.newInstance(transaction, result.getValue0(), result.getValue1(), feature);
                    value = method.invoke(instance);
                }
                else if("redis".equalsIgnoreCase(feature.computationtype)) {
                    constructor = classToLoad.getConstructor(Transaction.class, State.class, EnrichedTransaction.class, Feature.class, Jedis.class);
                    Object instance = constructor.newInstance(transaction, result.getValue0(), result.getValue1(), feature, jedis);
                    value = method.invoke(instance);
                }
                else if("psql".equalsIgnoreCase(feature.computationtype)) {
                    constructor = classToLoad.getConstructor(Transaction.class, State.class, EnrichedTransaction.class, Feature.class, Connection.class);
                    Object instance = constructor.newInstance(transaction, result.getValue0(), result.getValue1(), feature, connection);
                    value = method.invoke(instance);
                }
                return value;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Object invokeReflection(String featureClassName, Transaction transaction, Pair<State, EnrichedTransaction> result, String transferType) {
        try {
            //Class
            Class classToLoad = Class.forName(featureClassName);
            //Method
            Method method = classToLoad.getDeclaredMethod("compute");

            Constructor constructor = null;
            constructor = classToLoad.getConstructor(Transaction.class, State.class, EnrichedTransaction.class, String.class);

            Object instance = constructor.newInstance(transaction, result.getValue0(), result.getValue1(), transferType);
            Object value = method.invoke(instance);
            return value;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
